package com.github.lpandzic.junit.bdd;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Optional;

/**
 * The Bdd is a {@link TestRule JUnit test rule} which which provides a simple and fluent API that gives you a way of
 * structuring your test code within when and then blocks used in Behavior-driven development.
 *
 * <p>Testing with JUnit-BDD starts by defining a JUnit test class that contains following rule definition:
 * <pre>
 * import com.github.lpandzic.junit.bdd.Bdd;
 * import static com.github.lpandzic.junit.bdd.Bdd.when;
 *
 * &#064;Rule
 * public Bdd bdd = Bdd.initialized();
 * </pre>
 *
 * <p><strong>Return value assertion</strong></p>
 * For a given class {@code DeathStar} that contains method with signature {@code Target fireAt(Target target) throws
 * TargetAlreadyDestroyedException} where {@code TargetAlreadyDestroyedException} is a checked exception,
 * we can do the following value assertion:
 * <pre>{@code
 * when(deathStar.fireAt(alderaan)).then(target -> {
 *     assertThat(target.isDestroyed(), is(true));
 *     assertThat(target, is(alderaan));
 *     assertThat(target, is(not(coruscant)));
 * });
 * }</pre>
 *
 * <p><strong>Thrown exception assertion</strong></p>
 * In order to catch exception for an assertion we pass a lambda to the when block:
 * <pre>{@code
 * when(deathStar.fireAt(alderaan));
 * when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
 *     assertThat(thrownException, is(instanceOf(TargetAlreadyDestroyedException.class)));
 *     assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
 * });
 * }</pre>
 *
 * <p><strong>Thrown checked exceptions assertion</strong></p>
 * If we decide to change the {@code fireAt} method so that it doesn't throw the {@code
 * TargetAlreadyDestroyedException} the test mentioned in previous sub chapter will fail,
 * but it will still compile. Since {@code TargetAlreadyDestroyedException} is a checked exception we can use
 * Generics to prevent that test from compiling and reduce the time required to detect the error! To use this feature
 * change {@code then} to {@code thenChecked} and use {@code isA} matcher:
 * <pre>{@code
 * when(deathStar.fireAt(alderaan));
 * when(() -> deathStar.fireAt(alderaan)).thenChecked(thrownException -> {
 *     assertThat(thrownException, isA(TargetAlreadyDestroyedException.class));
 *     assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
 * });
 * }</pre>
 * <p>Now if we decide to change the signature of fireAt not to include TargetAlreadyDestroyedException we get a
 * compilation error.</p>
 *
 * <p><strong>Assertion framework flexibility</strong></p>
 * Although Hamcrest was used in previous examples you are free to use any Java assertion framework. For example,
 * the first testing example can be translated to:
 * <ul>
 * <li><a href="http://github.com/junit-team/junit/wiki/Assertions">plain JUnit assertions</a>
 * <pre>{@code
 * when(deathStar.fireAt(alderaan)).then(target -> {
 *     assertTrue(target.isDestroyed());
 *     assertEquals(target, alderaan);
 *     assertNotEquals(target, coruscant);
 * });
 * }</pre></li>
 * <li><a href="http://joel-costigliola.github.io/assertj/index.html">AssertJ</a>
 * <pre>{@code
 * when(deathStar.fireAt(alderaan)).then(target -> {
 *     assertThat(target.isDestroyed()).isTrue();
 *     assertThat(target).isEqualTo(alderaan);
 *     assertThat(target).isNotEqualTo(coruscant);
 * });
 * }</pre></li>
 * </ul>
 *
 * @author Lovro Pandzic
 * @see <a href="http://dannorth.net/introducing-bdd/">Introducing Bdd</a>
 * @see <a href="http://martinfowler.com/bliki/GivenWhenThen.html">GivenWhenThen article by M. Fowler</a>
 */
public final class Bdd implements TestRule {

    /**
     * Used for specifying behavior that should throw an exception.
     *
     * @param throwableSupplier supplier or throwable
     * @param <T>               the type of
     *
     * @return new {@link Then.Throws}
     */
    public static <T extends Exception> Then.Throws<T> when(ThrowableSupplier<T> throwableSupplier) {

        Bdd bdd = Bdd.bdd.get();

        if (bdd == null) {
            throw new IllegalStateException("Bdd rule not initialized");
        }

        bdd.requireThatNoUnexpectedExceptionWasThrown();

        return new When(bdd).when(throwableSupplier);
    }

    /**
     * Used for specifying behavior that should return a value.
     *
     * @param value returned by the specified behavior
     * @param <T>   type of {@code value}
     *
     * @return new {@link Then.Returns}
     */
    public static <T> Then.Returns<T> when(T value) {

        Bdd bdd = Bdd.bdd.get();

        if (bdd == null) {
            throw new IllegalStateException("Bdd rule not initialized");
        }

        bdd.requireThatNoUnexpectedExceptionWasThrown();

        return new When(bdd).when(value);
    }

    /**
     * Static factory method for {@link Bdd}.
     *
     * @return new bdd
     */
    public static Bdd initialized() {

        Bdd bdd = new Bdd();
        Bdd.bdd.set(bdd);
        return bdd;
    }

    private static final ThreadLocal<Bdd> bdd = new ThreadLocal<>();

    /**
     * Exception thrown in a {@link When} or {@link Optional#empty()}.
     */
    private Optional<Throwable> thrownException;

    @Override
    public Statement apply(Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                base.evaluate();
                requireThatNoUnexpectedExceptionWasThrown();
            }
        };
    }

    /**
     * Inserts the {@code throwable} into {@link #thrownException}.
     *
     * @param throwable to add
     */
    void putThrownException(Throwable throwable) {

        requireThatNoUnexpectedExceptionWasThrown();

        thrownException = Optional.of(throwable);
    }

    /**
     * Retrieves and removes {@link #thrownException}.
     *
     * Used by {@link Then} for consuming {@link #thrownException}
     *
     * @return {@link #thrownException}
     */
    Optional<Throwable> takeThrownException() {

        Optional<Throwable> thrownException = this.thrownException;
        this.thrownException = Optional.empty();
        return thrownException;
    }

    void throwUnexpectedException(Throwable throwable) {

        throw new IllegalStateException("Unexpected exception was thrown", throwable);
    }

    /**
     * Throws {@link #thrownException} if present.
     *
     * @throws IllegalStateException if a {@code thrownException} already contains an exception,
     *                               the previous thrown exception is wrapped
     */
    private void requireThatNoUnexpectedExceptionWasThrown() {

        if (thrownException.isPresent()) {
            throwUnexpectedException(thrownException.get());
        }
    }

    private Bdd() {

        thrownException = Optional.empty();
    }
}
