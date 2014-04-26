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

    void throwUnexpectedException(Throwable throwable) {
        throw new IllegalStateException("Unexpected exception was thrown", throwable);
    }

    private Bdd() {

        thrownException = Optional.empty();
    }
}
