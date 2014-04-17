package com.github.lpandzic.junit.bdd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

/**
 * @author Lovro Pandzic
 */
public class FeatureTest {

    @Rule
    public Bdd bdd = Bdd.initialized();

    @Rule
    public ExpectedException expectedException = ExpectedException.none().handleAssertionErrors();

    private ClassUnderTest classUnderTest = new ClassUnderTest();

    @Test
    public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

        when(() -> classUnderTest.throwsA(new Exception())).thenChecked(e -> assertThat(e, isA(Exception.class)));
    }

    @Test
    public void shouldFailWhenWrongExceptionIsThrown() throws Exception {

        expectedException.handleAssertionErrors();
        expectedException.expect(AssertionError.class);

        when(() -> classUnderTest.throwsA(new IOException())).then(e -> assertThat(e, is(instanceOf(NullPointerException
                                                                                                            .class))
                                                                                  ));
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

    @Test(expected = Exception.class)
    public void shouldFailWhenValueProviderThrowsAUncheckedException() throws Exception {

        when(classUnderTest.throwsA(new Exception()));
    }

    @Test
    public void shouldFailWhenExceptionProviderThrowsAUncheckedException() throws Exception {

        expectedException.expect(Exception.class);

        when(() -> classUnderTest.throwsA(new Exception()));
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

    private static class ClassUnderTest {

        public <T extends Throwable> Void throwsA(T exception) throws T {

            throw exception;
        }

        public <T> T returnsA(T value) {

            return value;
        }
    }
}