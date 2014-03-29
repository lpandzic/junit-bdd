package com.github.lpandzic.junit.bdd;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

/**
 * @author Lovro Pandzic
 */
public class BddTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none().handleAssertionErrors();
    private ClassUnderTest classUnderTest = new ClassUnderTest();

    @Test
    public void shouldPassWhenNoExceptionIsThrown() throws Exception {

        when(() -> classUnderTest.returnsA(null)).thenShouldThrowExceptionThat(is(CoreMatchers.nullValue(Exception.class)));
    }

    @Test
    public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception())).thenShouldThrowCheckedExceptionThat(isA(Exception.class));
    }

    @Test
    public void shouldFailWhenWrongExceptionIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(() -> classUnderTest.throwsA(new IOException())).thenShouldThrowExceptionThat(isA(NullPointerException.class));
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectMessageIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception("abc"))).thenShouldThrowCheckedExceptionThat(isA(Exception.class)).withMessageThat(is(equalTo("abc")));
    }

    @Test
    public void shouldFailWhenExceptionWithWrongMessageIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(() -> classUnderTest.throwsA(new Exception("abc"))).thenShouldThrowCheckedExceptionThat(isA(Exception.class)).withMessageThat(is(equalTo("def")));
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectCauseIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception(new NullPointerException()))).thenShouldThrowCheckedExceptionThat(isA(Exception.class))
                                                                                     .withCauseThat(isA(NullPointerException.class));
    }

    @Test
    public void shouldFailWhenExceptionWithIncorrectCauseIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(() -> classUnderTest.throwsA(new Exception(new IllegalArgumentException()))).thenShouldThrowCheckedExceptionThat(isA(Exception.class))
                                                                                         .withCauseThat(isA(NullPointerException.class));
    }

    @Test
    public void shouldPassWhenExceptionWithCorrectMessageAndCauseIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception("abc", new NullPointerException()))).thenShouldThrowCheckedExceptionThat(isA(Exception.class))
                                                                                            .withMessageThat(is(equalTo("abc")))
                                                                                            .andCauseThat(isA(NullPointerException.class));
    }

    @Test
    public void shouldPassWhenReturnsExpectedObject() {

        Object value = new Object();
        when(classUnderTest.returnsA(value)).thenShouldReturnValueThat(is(equalTo(value)));
    }

    @Test
    public void simpleLambdaThrowTest() {

        when(() -> {
            throw new Exception();
        }).thenShouldThrowCheckedExceptionThat(isA(Exception.class));
    }

    @Test
    public void simpleLambdaReturnTest() {

        when(new Object()).thenShouldReturnValueThat(isA(Object.class));
    }

    @Test
    public void shouldFailWhenReturnsOneObjectButExpectsDifferentObject() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("is <java.lang.Object");
        expectedException.expectMessage("but: was");

        Object value = new Object();
        when(classUnderTest.returnsA(value)).thenShouldReturnValueThat(is(equalTo(new Object())));
    }

    @Test
    public void shouldPassWhenReturnsExpectedNull() {

        when(classUnderTest.<Object>returnsA(null)).thenShouldReturnValueThat(is(equalTo(null)));
    }

    private static class ClassUnderTest {

        public <T extends Throwable> void throwsA(T exception) throws T {

            throw exception;
        }

        public <T> T returnsA(T value) {

            return value;
        }
    }
}