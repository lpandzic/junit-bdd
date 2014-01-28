package com.github.lpandzic.junit.bdd;

import org.junit.Before;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.util.List;

import static com.github.lpandzic.chameleon.Chameleon.mimic;

/**
 * The Bdd is a {@link TestRule JUnit test rule} which enables Behavior-driven development (BDD) style testing.
 *
 * <p>For construction use either {@link #initialized(Object)} or {@link #notInitialized()}.</p>
 *
 * <p>If {@link UnderTest} needs to be initialized in the {@link Before} method then {@link #notInitialized()} should
 * be
 * used in combination with {@link #initialize(Object)}.</p>
 *
 * <p>Example usage of {@link #initialized(Object)}:
 * <pre>
 * public class TestClassExample {
 *
 *     <b>&#064;UnderTest</b>
 *     private ClassUnderTest classUnderTest = new ClassUnderTest(...);
 *
 *     // <b>note that Bdd is declared after the @UnderTest</b>
 *     <b>&#064;Rule</b>
 *     public Bdd bdd = Bdd.initialized(this);
 * }</pre></p>
 *
 * <p>Example usage of {@link #notInitialized()} in combination with {@link #initialize(Object)}:
 * <pre>
 * public class TestClassExample {
 *     <b>&#064;UnderTest</b>
 *     private ClassUnderTest classUnderTest;
 *
 *     <b>&#064;Rule</b>
 *     public Bdd bdd = Bdd.notInitialized();
 *
 *     &#064;Before
 *     public void setUp() {
 *         classUnderTest = new ClassUnderTest(...);
 *         // <b>note that the bdd is initialized after the @UnderTest</b>
 *         bdd.initialize(this);
 *     }
 * }</pre></p>
 *
 * <p>The only reason why {@code UnderTest} needs to be initialized before {@link Bdd} is because Bdd needs to wrap the
 * value of {@code UnderTest} field in a dynamic proxy.</p>
 *
 * <p>For more information about usage see {@link BddJunit}.</p>
 *
 * @author Lovro Pandzic
 * @see <a href="http://dannorth.net/introducing-bdd/">Introducing Bdd</a>
 * @see <a href="http://martinfowler.com/bliki/GivenWhenThen.html">GivenWhenThen article by M. Fowler</a>
 * @see BddJunit
 * @see UnderTest
 * @see Then
 */
public final class Bdd implements TestRule {

    /**
     * Instance of the JUnit test class.
     */
    private Object testObject;

    /**
     * Field in the {@link #testObject testObject} annotated with {@link UnderTest}.
     */
    private Field underTestField;

    /**
     * Original value of the {@link #underTestField}.
     */
    private Object target;

    /**
     * Proxy wrapping the {@link #target}. Behavior is specified in the {@link #bddProxyController}.
     */
    private Object chameleon;

    /**
     * Specifies the behavior of the {@link #chameleon}.
     */
    private BddProxyController bddProxyController;

    /**
     * Static factory method for simpler and preferred way of constructing {@link Bdd} but to use it the following
     * preconditions must be satisfied:
     * <ul>
     * <li>Bdd must be declared after the @UnderTest in the test class.</li>
     * <li>@UnderTest field must be initialized before calling this method.</li>
     * </ul>
     *
     * For more information and examples see {@link Bdd bdd javadoc}.
     *
     * @param testObject instance of the JUnit test class
     *
     * @return initialized {@code bdd}
     *
     * @throws NullPointerException if {@code testObject} is null
     * @throws BddException         if invoked more than once or if {@link UnderTest} field value in {@code testObject}
     *                              is null
     */
    public static Bdd initialized(Object testObject) {

        Bdd bdd = notInitialized();
        bdd.initialize(testObject);
        return bdd;
    }

    /**
     * Returns new {@link Bdd bdd} that is not initialized. To initialize use {@link #initialize(Object)}.
     * For more information see {@link Bdd bdd javadoc}.
     *
     * @return new {@code bdd} in uninitialized state
     */
    public static Bdd notInitialized() {

        Bdd bdd = new Bdd();
        BddJunit.set(bdd);
        return bdd;
    }

    /**
     * Initializes the {@link Bdd} for the given {@code testObject}.
     *
     * @param testObject instance of the JUnit test class.
     *
     * @throws NullPointerException if {@code testObject} is null
     * @throws BddException         if invoked more than once or if {@link UnderTest} field value in {@code testObject}
     *                              is null
     */
    public void initialize(Object testObject) {

        if (testObject == null) {
            throw new NullPointerException("testObject is null");
        }

        if (isInitialized()) {
            throw new BddException("already initialized");
        }

        Field underTestField = scanForUnderTestField(testObject);

        Object target = ReflectionUtils.getFieldValue(underTestField, testObject);

        if (target == null) {
            throw new BddException("value of @UnderTest field is null");
        }

        bddProxyController = new BddProxyController();
        this.testObject = testObject;
        this.underTestField = underTestField;
        this.target = target;
        chameleon = mimic(target, bddProxyController);
        ReflectionUtils.setFieldValue(underTestField, testObject, chameleon);
    }

    @Override
    public Statement apply(final Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                try {
                    base.evaluate();
                    requireThatNoUnexpectedExceptionWasThrown();
                } finally {
                    returnTargetObjectToUnderTestField();
                    BddJunit.remove();
                }
            }
        };
    }

    /**
     * Used for expressing tested behavior (when) of the {@code underTest}.
     *
     * @param underTest instance of the class under test annotated with {@link UnderTest}
     * @param <T>       class under test type
     *
     * @return {@code underTest}
     */
    <T> T when(T underTest) {

        requireInitialized();
        requireUnderTestIsValid(underTest);
        requireThatUnderTestIsProxy(underTest);

        return underTest;
    }

    /**
     * Validates bdd state and then returns {@link BddProxyController#then()}.
     */
    Then then(Object underTest) {

        requireInitialized();
        requireUnderTestIsValid(underTest);
        requireThatUnderTestIsProxy(underTest);

        return bddProxyController.then();
    }

    void returnTargetObjectToUnderTestField() {

        if (!isInitialized()) {
            return;
        }

        ReflectionUtils.setFieldValue(underTestField, testObject, target);
    }

    /**
     * Validates that the proxy
     */
    private void requireInitialized() {

        if (bddProxyController == null) {
            throw new BddException("bdd is not properly initialized.");
        }
    }

    /**
     * Validates that the {@code underTest} is of the same type as the {@link UnderTest class under test}.
     *
     * @param underTest object being validated
     *
     * @throws BddException if the {@code underTest} is not of the same type as the {@link Bdd#target}
     */
    private void requireUnderTestIsValid(Object underTest) {

        if (!target.getClass().isAssignableFrom(underTest.getClass())) {
            throw new BddException("underTest is not of the correct type: expected " +
                                   target.getClass().getName() +
                                   " but was " +
                                   underTest.getClass().getName());
        }
    }

    /**
     * Validates that the {@code underTest} is the same object as the proxy.
     *
     * @param underTest object being validated
     *
     * @throws BddException if the {@code underTest} is not {@link #chameleon proxy}
     */
    private void requireThatUnderTestIsProxy(Object underTest) {

        if (underTest != chameleon) {
            throw new BddException("invalid argument passed to when, expected " +
                                   chameleon +
                                   " but received " +
                                   underTest);
        }
    }

    /**
     * @see BddProxyController#requireNoUnexpectedExceptionWasThrown()
     */
    private void requireThatNoUnexpectedExceptionWasThrown() throws Throwable {

        bddProxyController.requireNoUnexpectedExceptionWasThrown();
    }

    private Field scanForUnderTestField(Object testObject) {

        List<Field> matchedFields = ReflectionUtils.findAnnotatedFields(testObject, UnderTest.class);

        if (matchedFields.size() != 1) {
            throw new BddException("there must be exactly one field marked with @UnderTest in the " +
                                   testObject.getClass().getSimpleName() + " but there were " + matchedFields.size());
        }

        return matchedFields.get(0);
    }

    /**
     * @return true if initialized, false otherwise
     */
    private boolean isInitialized() {

        return bddProxyController != null;
    }

    /**
     * For construction see {@link Bdd}.
     */
    private Bdd() {

    }
}
