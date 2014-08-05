package com.github.lpandzic.junit.bdd;

import com.github.lpandzic.bdd4j.Bdd4j;
import com.github.lpandzic.bdd4j.Then;
import com.github.lpandzic.bdd4j.ThrowableSupplier;

/**
 * Bdd provides a simple and fluent API for structuring test code within when and then blocks used in Behavior-driven
 * development.
 *
 * <p>Following static import is useful for simpler syntax when using JUnit-BDD:
 * <pre>
 * import static com.github.lpandzic.junit.bdd.Bdd.when;
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
 * <ol>
 * <li>Return value assertion
 * <pre>{@code
 * when(deathStar.fireAt(alderaan)).then(target -> {
 *     assertTrue(target.isDestroyed());
 *     assertEquals(target, alderaan);
 *     assertNotEquals(target, coruscant);
 * });
 * }</pre></li>
 * <li>Thrown exception assertion
 * <pre>{@code
 * when(deathStar.fireAt(alderaan));
 * when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
 *     assertEquals(TargetAlreadyDestroyedException.class, thrownException.getClass());
 *     assertEquals("Cannot fire at a destroyed " + alderaan, thrownException.getMessage());
 * });
 * }</pre></li>
 * </ol>
 * <li><a href="http://joel-costigliola.github.io/assertj/index.html">AssertJ</a>
 * <ol>
 * <li>Return value assertion
 * <pre>{@code
 * when(deathStar.fireAt(alderaan)).then(target -> {
 *     assertThat(target.isDestroyed()).isTrue();
 *     assertThat(target).isEqualTo(alderaan);
 *     assertThat(target).isNotEqualTo(coruscant);
 * });
 * }</pre></li>
 * <li>Thrown exception assertion
 * <pre>{@code
 * when(deathStar.fireAt(alderaan));
 * when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
 *     assertThat(thrownException).isExactlyInstanceOf(TargetAlreadyDestroyedException.class);
 *     assertThat(thrownException.getMessage()).isEqualTo("Cannot fire at a destroyed " + alderaan);
 * });
 * }</pre></li>
 * </ol>
 * </li>
 * </ul>
 *
 * @author Lovro Pandzic
 * @see <a href="http://dannorth.net/introducing-bdd/">Introducing Bdd</a>
 * @see <a href="http://martinfowler.com/bliki/GivenWhenThen.html">GivenWhenThen article by M. Fowler</a>
 * @deprecated As of 2.1, replaced by {@link Bdd4j}.
 */
@Deprecated
public final class Bdd {

    /**
     * Used for specifying behavior that should throw an throwable.
     *
     * <p><strong>Note: Not defining then inside the test after calling this method will cause throwable to be
     * silently swallowed and can cause subsequent test to fail. See {@link Bdd4j#when(ThrowableSupplier)} for more details.
     * }</strong></p>
     *
     * @param throwableSupplier supplier or throwable
     * @param <T>               the type of
     *
     * @return new {@link Then.Throws}
     *
     * @deprecated As of 2.1, replaced by {@link Bdd4j#when(ThrowableSupplier)}.
     */
    @Deprecated
    public static <T extends Exception> Then.Throws<T> when(ThrowableSupplier<T> throwableSupplier) {

        return Bdd4j.when(throwableSupplier);
    }

    /**
     * Used for specifying behavior that should return a value.
     *
     * @param value returned by the specified behavior
     * @param <T>   type of {@code value}
     *
     * @return new {@link Then.Returns}
     *
     * @deprecated As of 2.1, replaced by {@link Bdd4j#when(Object)}.
     */
    @Deprecated
    public static <T> Then.Returns<T> when(T value) {

        return Bdd4j.when(value);
    }

    private Bdd() {

    }
}
