package com.github.lpandzic.junit.bdd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static com.github.lpandzic.junit.bdd.BddJunit.then;
import static com.github.lpandzic.junit.bdd.BddJunit.when;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.willThrow;

/**
 * @author Lovro Pandzic
 */
@RunWith(MockitoJUnitRunner.class)
public class BddTest {

    @InjectMocks
    @UnderTest
    private ClassUnderTest classUnderTest;

    @Rule
    public Bdd bdd = Bdd.notInitialized();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ExceptionThrower exceptionThrower;

    @Before
    public void setUp() {

        bdd.initialize(this);
    }

    @Test
    public void shouldPassWhenNoExceptionIsThrown() throws Exception {

        when(classUnderTest).canThrow();
    }

    @Test
    public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

        willThrow(new Exception()).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class);
    }

    @Test
    public void shouldFailWhenWrongExceptionIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        willThrow(new IOException()).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(NullPointerException.class);
    }

    @Test
    public void shouldFailWhenUnexpectedExceptionIsThrown() throws Exception {

        expectedException.expect(IOException.class);

        willThrow(new IOException()).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();
    }

    @Test
    public void shouldFailWhenMultipleInstancesOfExpectedExceptionAreThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("UnderTest class threw more than one exception!");

        willThrow(new Exception()).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();
        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class);
    }

    @Test
    public void shouldFailWhenMultipleInstancesOfUnexpectedExceptionAreThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("UnderTest class threw more than one exception!");

        willThrow(new Exception()).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();
        when(classUnderTest).canThrow();
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectMessageIsThrown() throws Exception {

        willThrow(new Exception("abc")).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class).withMessage("abc");
    }

    @Test
    public void shouldFailWhenExceptionWithWrongMessageIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(
                "Expected: (an instance of java.lang.Exception and exception with message a string containing \"def\")");

        willThrow(new Exception("abc")).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class).withMessage("def");
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectCauseIsThrown() throws Exception {

        willThrow(new Exception(new NullPointerException())).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class).withCause(isA(NullPointerException.class));
    }

    @Test
    public void shouldFailWhenExceptionWithIncorrectCauseIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(
                "Expected: (an instance of java.lang.Exception and exception with cause is an instance of java.lang.NullPointerException)");

        willThrow(new Exception(new IllegalArgumentException())).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class).withCause(isA(NullPointerException.class));
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectMessageAndCauseIsThrown() throws Exception {

        willThrow(new Exception("abc", new NullPointerException())).given(exceptionThrower).testMethod();

        when(classUnderTest).canThrow();

        then(classUnderTest).shouldThrow(Exception.class).withMessage("abc").andCause(isA(NullPointerException.class));
    }

    @Test
    public void shouldPassWhenReturnsExpectedObject() {

        Object value = new Object();
        when(classUnderTest).returns(value);

        then(classUnderTest).shouldReturn(value);
    }

    @Test
    public void shouldFailWhenReturnsOneObjectButExpectsDifferentObject() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected");
        expectedException.expectMessage("but was");

        Object value = new Object();
        when(classUnderTest).returns(value);

        then(classUnderTest).shouldReturn(new Object());
    }

    @Test
    public void shouldPassWhenReturnsExpectedNull() {

        when(classUnderTest).returns(null);

        then(classUnderTest).shouldReturnNull();
    }

    @Test
    public void shouldFailWhenReturnsNonNullButExpectsNull() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected null");

        when(classUnderTest).returns(new Object());

        then(classUnderTest).shouldReturnNull();
    }

    @Test
    public void shouldPassWhenReturnsObjectAndExpectsNonNull() {

        when(classUnderTest).returns(new Object());

        then(classUnderTest).shouldNotReturnNull();
    }

    @Test
    public void shouldFailWhenReturnsNullAndExpectsNonNull() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(classUnderTest).returns(null);

        then(classUnderTest).shouldNotReturnNull();
    }

    @Test
    public void shouldPassWhenReturnsExpectedTrue() {

        when(classUnderTest).returns(true);

        then(classUnderTest).shouldReturnTrue();
    }

    @Test
    public void shouldFailWhenReturnsFalseButExpectsTrue() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected:<true>");
        expectedException.expectMessage("but was:<false>");

        when(classUnderTest).returns(false);

        then(classUnderTest).shouldReturnTrue();
    }

    @Test
    public void shouldPassWhenReturnsExpectedFalse() {

        when(classUnderTest).returns(false);

        then(classUnderTest).shouldReturnFalse();
    }

    @Test
    public void shouldFailWhenReturnsTrueButExpectsFalse() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected:<false>");
        expectedException.expectMessage("but was:<true>");

        when(classUnderTest).returns(false);

        then(classUnderTest).shouldReturnFalse();
    }

    @Test
    public void shouldPassWhenReturnsOneObjectAndExpectsNotToReturnDifferentObject() {

        when(classUnderTest).returns(false);

        then(classUnderTest).shouldNotReturn(true);
    }

    @Test
    public void shouldFailWhenReturnsOneObjectAndExpectsNotToReturnThatObject() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Values should be different.");

        when(classUnderTest).returns(true);

        then(classUnderTest).shouldNotReturn(true);
    }

    @Test
    public void shouldPassWhenReturnsExpectedObjectArray() throws Exception {

        when(classUnderTest).returns(new Integer[]{1, 2, 3});

        then(classUnderTest).shouldReturnArray(new Integer[]{1, 2, 3});
    }

    @Test
    public void shouldFailWhenReturnsOneObjectArrayButExpectsDifferentObjectArray() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("array lengths differed");

        when(classUnderTest).returns(new Integer[]{1, 2, 3});

        then(classUnderTest).shouldReturnArray(new Integer[]{1, 2, 3, 4});
    }

    @Test
    public void shouldFailWhenReturnsNonArrayButExpectsArray() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("expected Object[] but was Object");

        when(classUnderTest).returns(new Object());

        then(classUnderTest).shouldReturnArray(new Integer[]{1, 2, 3});
    }

    @Test
    public void shouldFailIfArgumentPassedToWhenIsOfDifferentTypeThanClassUnderTest() throws Exception {

        expectedException.expect(BddException.class);
        expectedException.expectMessage(String.format("underTest is not of the correct type: expected %s but was %s",
                                                      ClassUnderTest.class.getName(),
                                                      Object.class.getName()));

        when(new Object());
    }

    @Test
    public void shouldFailIfArgumentPassedToWhenIsNotTheProxy() throws Exception {

        ClassUnderTest newClassUnderTest = new ClassUnderTest(null);

        expectedException.expect(BddException.class);
        expectedException.expectMessage(String.format("invalid argument passed to when, expected %s but received %s",
                                                      classUnderTest,
                                                      newClassUnderTest));

        when(newClassUnderTest);
    }

    public static class BddIncorrectUsageTest {

        @UnderTest
        private ClassUnderTest classUnderTest;

        @Rule
        public Bdd bdd = Bdd.notInitialized();

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void shouldFailOnWhenWhenNotInitialized() throws Exception {

            expectedException.expect(BddException.class);
            expectedException.expectMessage("bdd is not properly initialized.");

            when(classUnderTest).canThrow();
        }

        @Test
        public void shouldFailOnThenWhenNotInitialized() throws Exception {

            expectedException.expect(BddException.class);
            expectedException.expectMessage("bdd is not properly initialized.");

            then(classUnderTest);
        }

        @Test
        public void shouldFailWhenInitializingTwice() throws Exception {

            classUnderTest = new ClassUnderTest(null);

            bdd.initialize(this);

            expectedException.expect(BddException.class);
            expectedException.expectMessage("already initialized");
            bdd.initialize(this);
        }

        @Test
        public void shouldFailWhenUnderTestFieldIsNull() throws Exception {

            expectedException.expect(BddException.class);
            expectedException.expectMessage("value of @UnderTest field is null");

            bdd.initialize(this);
        }
    }

    public static class TooManyUnderTestFieldsTest {

        @UnderTest
        private ClassUnderTest classUnderTest;

        @UnderTest
        private ClassUnderTest classUnderTest2;

        @Rule
        public Bdd bdd = Bdd.notInitialized();

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void shouldFailIfThereIsMoreThanOneFieldAnnotatedWithUnderTest() throws Exception {

            expectedException.expect(BddException.class);
            expectedException.expectMessage(
                    "there must be exactly one field marked with @UnderTest in the TooManyUnderTestFieldsTest but there were 2");

            bdd.initialize(this);
        }
    }

    public static class BddInitializedTest{

        @UnderTest
        private ClassUnderTest classUnderTest = new ClassUnderTest(null);

        @Rule
        public Bdd bdd = Bdd.initialized(this);

        @Test
        public void shouldReturnNullWhenNullIsReturned() throws Exception {

            when(classUnderTest).returns(null);

            then(classUnderTest).shouldReturnNull();
        }
    }

    private interface ExceptionThrower {

        void testMethod() throws Exception;
    }

    private static class ClassUnderTest {

        private final ExceptionThrower exceptionThrower;

        private ClassUnderTest(ExceptionThrower exceptionThrower) {

            this.exceptionThrower = exceptionThrower;
        }

        public void canThrow() throws Exception {

            exceptionThrower.testMethod();
        }

        public <T> T returns(T value) {

            return value;
        }
    }
}