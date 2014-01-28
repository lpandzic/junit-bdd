package com.github.lpandzic.junit.bdd;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.ThrowableCauseMatcher.hasCause;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * Used to describe expected outcome of {@link BddJunit#when(Object) when}  call on {@link UnderTest class under test}.
 *
 * @author Lovro Pandzic
 */
public final class Then {

    /**
     * Used for notifying exception verification result.
     */
    private final BddProxyController bddProxyController;

    /**
     * @see BddProxyController#thrownException
     */
    private final Throwable thrownException;

    /**
     * @see BddProxyController#returnValue
     */
    private final Object actualResult;

    Then(BddProxyController bddProxyController, Throwable thrownException, Object actualResult) {

        this.bddProxyController = bddProxyController;
        this.thrownException = thrownException;
        this.actualResult = actualResult;
    }

    public Throws shouldThrow(Class<? extends Throwable> type) {

        return new Throws(type);
    }

    public void shouldReturn(Object expected) {

        assertEquals(expected, actualResult);
    }

    public void shouldReturnTrue() {

        assertEquals(true, actualResult);
    }

    public void shouldReturnFalse() {

        assertEquals(false, actualResult);
    }

    public void shouldReturnNull() {

        assertNull(actualResult);
    }

    public void shouldNotReturnNull() {

        assertNotNull(actualResult);
    }

    public void shouldNotReturn(Object value) {

        assertNotEquals(value, actualResult);
    }

    public void shouldReturnArray(Object[] array) {

        if (!(actualResult instanceof Object[])) {
            fail(String.format("expected %s but was %s",
                               Object[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((Object[]) actualResult, array);
    }

    public void shouldReturnArray(byte[] array) {

        if (!(actualResult instanceof byte[])) {
            fail(String.format("expected %s but was %s",
                               byte[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((byte[]) actualResult, array);
    }

    public void shouldReturnArray(char[] array) {

        if (!(actualResult instanceof char[])) {
            fail(String.format("expected %s but was %s",
                               char[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((char[]) actualResult, array);
    }

    public void shouldReturnArray(short[] array) {

        if (!(actualResult instanceof short[])) {
            fail(String.format("expected %s but was %s",
                               short[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((short[]) actualResult, array);
    }

    public void shouldReturnArray(int[] array) {

        if (!(actualResult instanceof int[])) {
            fail(String.format("expected %s but was %s",
                               int[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((int[]) actualResult, array);
    }

    public void shouldReturnArray(long[] array) {

        if (!(actualResult instanceof long[])) {
            fail(String.format("expected %s but was %s",
                               long[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((long[]) actualResult, array);
    }

    public void shouldReturnArray(float[] array, float delta) {

        if (!(actualResult instanceof float[])) {
            fail(String.format("expected %s but was %s",
                               float[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((float[]) actualResult, array, delta);
    }

    public void shouldReturnArray(double[] array, double delta) {

        if (!(actualResult instanceof double[])) {
            fail(String.format("expected %s but was %s",
                               double[].class.getSimpleName(),
                               actualResult.getClass().getSimpleName()));
        }

        assertArrayEquals((double[]) actualResult, array, delta);
    }

    public final class Throws {

        private final ExpectedThrowableMatcher expectedThrowableMatcher;

        private Throws(Class<? extends Throwable> type) {

            expectedThrowableMatcher = new ExpectedThrowableMatcher();
            expect(type);
        }

        void expect(Class<? extends Throwable> type) {

            expect(instanceOf(type));
        }

        void expectMessage(String substring) {

            expectMessage(containsString(substring));
        }

        void expectMessage(Matcher<String> matcher) {

            expect(hasMessage(matcher));
        }

        void expectCause(Matcher<? extends Throwable> expectedCause) {

            expect(hasCause(expectedCause));
        }

        private void expect(Matcher<?> matcher) {

            expectedThrowableMatcher.add(matcher);
            assertThat(thrownException, expectedThrowableMatcher.build());
            bddProxyController.onThrowableValidatedSuccessfully();
        }

        public Message withMessage(String substring) {

            expectMessage(substring);
            return new Message();
        }

        public Message withMessage(Matcher<String> matcher) {

            expectMessage(matcher);
            return new Message();
        }

        public Cause withCause(Matcher<? extends Throwable> expectedCause) {

            expectCause(expectedCause);
            return new Cause();
        }

        public final class Message {

            private Message() {

            }

            public void andCause(Matcher<? extends Throwable> expectedCause) {

                Throws.this.withCause(expectedCause);
            }

        }

        public final class Cause {

            private Cause() {

            }

            public void andMessage(String substring) {

                Throws.this.withMessage(substring);
            }

            public void andMessage(Matcher<String> matcher) {

                Throws.this.withMessage(matcher);
            }

        }
    }

}
