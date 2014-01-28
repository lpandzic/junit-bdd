package com.github.lpandzic.junit.bdd;

import org.junit.Before;
import org.junit.Test;

/**
 * Provides static factory methods associated with the {@link Bdd}.
 *
 * <p><strong>Note that before using the Bdd it must be initialized with either {@link Bdd#initialized(Object)} or
 * {@link Bdd#initialize(Object)}.</strong></p>
 *
 * <p>Bdd style testing of thrown exception:
 * <pre>
 * public class TestClassExample {
 *
 *     &#064;UnderTest
 *     private ClassUnderTest classUnderTest = new ClassUnderTest(...);
 *
 *     &#064;Rule
 *     public Bdd bdd = Bdd.initialized(this);
 *
 *     &#064;Test
 *     public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {
 *
 *         when(classUnderTest).methodThatThrowsException();
 *
 *         then(classUnderTest).shouldThrow(Exception.class);
 *     }
 * }</pre></p>
 *
 * <p>Bdd style testing of return value:
 * <pre>
 * public class TestClassExample {
 *
 *     &#064;UnderTest
 *     private ClassUnderTest classUnderTest = new ClassUnderTest(...);
 *
 *     &#064;Rule
 *     public Bdd bdd = Bdd.initialized(this);
 *
 *     &#064;Test
 *     public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {
 *
 *         when(classUnderTest).returnsSomeValue();
 *
 *         then(classUnderTest).shouldReturn(expectedValue);
 *     }
 * }</pre></p>
 *
 *
 * <p>If you are using <a href="https://github.com/mockito/mockito">Mockito</a> of version 1.8.0 or higher, {@code
 * BDDMockito} integrates nicely with {@code BddJunit} {@link #when(Object)} and {@link #then(Object)} methods for
 * improved readability and clarity.</p>
 *
 * <p>Keeps {@code bdd} in a static {@link ThreadLocal} {@link #bdd variable} to enable simpler and cleaner syntax.</p>
 *
 * @author Lovro Pandzic
 * @see Bdd
 * @see BddJunit
 */
public final class BddJunit {

    /**
     * Initializes the {@link Bdd} for the given {@code testObject}.
     *
     * <p>Call this method only after the {@link UnderTest class under test} field in {@code testObject} has been set.
     * Usually this method is called from the {@link Before} method.
     *
     * @param testObject object with {@link Test Test} annotated methods.
     */

    /**
     * Syntactic sugar method for expressing tested behavior (when) of the {@code underTest}.
     *
     * @param underTest instance of the class under test annotated with {@link UnderTest}
     * @param <T>       class under test type
     *
     * @return {@code underTest}
     */
    public static <T> T when(T underTest) {

        Bdd bdd = getBdd();

        return bdd.when(underTest);
    }

    public static Then then(Object underTest) {

        Bdd bdd = getBdd();

        return bdd.then(underTest);
    }

    /**
     * Keeps reference to the {@link Bdd} for the test.
     */
    private static final ThreadLocal<Bdd> bdd = new ThreadLocal<Bdd>();

    /**
     * Sets the {@link Bdd} value for this thread.
     *
     * @param value to set
     */
    static void set(Bdd value) {

        bdd.set(value);
    }

    /**
     * Removes the {@link Bdd} value for this thread.
     */
    static void remove() {

        bdd.remove();
    }

    /**
     * Gets the {@link Bdd bdd} associated with the current test.
     *
     * @return the {@link Bdd bdd} associated with the current test or null if test was not properly initialized
     */
    private static Bdd getBdd() {

        return BddJunit.bdd.get();
    }

    private BddJunit() {

    }
}
