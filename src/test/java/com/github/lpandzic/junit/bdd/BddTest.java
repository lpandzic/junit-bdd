package com.github.lpandzic.junit.bdd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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

        when(() -> classUnderTest.returnsA(null)).then((NullPointerException e) -> assertThat(e,
                                                                                              isA((NullPointerException.class))));
    }

    @Test
    public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception())).thenChecked(e -> assertThat(e, isA(Exception.class)));
    }

    @Test
    public void shouldFailWhenWrongExceptionIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(() -> classUnderTest.throwsA(new IOException())).then((NullPointerException e) -> assertThat(e,
                                                                                                          isA(NullPointerException.class)));
    }

    @Test
    public void shouldPassWhenReturnsExpectedObject() {

        Object value = new Object();
        when(classUnderTest.returnsA(value)).then(actual -> assertThat(actual, is(equalTo(value))));
    }

    @Test
    public void simpleLambdaThrowTest() {

        when(() -> {
            throw new Exception();
        }).thenChecked(e -> assertThat(e, isA(Exception.class)));
    }

    @Test
    public void simpleLambdaReturnTest() {

        when(new Object()).then(actual -> assertThat(actual, isA(Object.class)));
    }

    @Test
    public void shouldFailWhenReturnsOneObjectButExpectsDifferentObject() {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("is <java.lang.Object");
        expectedException.expectMessage("but: was");

        Object value = new Object();
        when(classUnderTest.returnsA(value)).then(actual -> assertThat(actual, is(equalTo(new Object()))));
    }

    @Test
    public void shouldPassWhenReturnsExpectedNull() {

        when(classUnderTest.returnsA(new Object())).then(actual -> assertThat(actual, is(equalTo(null))));
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