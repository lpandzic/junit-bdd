package com.github.lpandzic.junit.bdd;

import com.github.lpandzic.chameleon.Chameleon;
import com.github.lpandzic.chameleon.MimicStrategy;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.fail;

/**
 * Controls the behavior of the proxy which wraps {@link UnderTest the class under test}.
 *
 * <p>Result of each method call on the class under test is stored and used for assertions in {@link Then}. All exceptions
 * thrown are intercepted and stored to enable assertion of thrown exception after the method has been called. Each new
 * method call return value overwrites the last one and multiple thrown exceptions are not supported so tests will fail
 * fast if more than one exception is thrown.</p>
 *
 * @author Lovro Pandzic
 * @see MimicStrategy
 * @see Chameleon
 */
final class BddProxyController<T> implements MimicStrategy<T> {

    /**
     * Value returned by the last method call which did not throw an exception or null otherwise.
     */
    private Object returnValue;

    /**
     * Exception thrown by the last method call which threw an exception or null otherwise.
     */
    private Throwable thrownException;

    /**
     * Boolean flag indicating whether {@link #thrownException} has been validated.
     */
    private boolean throwableHasNotBeenValidated = true;

    /**
     * Calls the method on the {@code target} and stores the result of that call.
     *
     * @param target         proxied object
     * @param mimickedMethod method invoked by the client
     * @param arguments      arguments passed to the {@code mimickedMethod}
     *
     * @return return value of the mimicked method
     *
     * @throws IllegalAccessException if the underlying method throws an exception
     * @throws AssertionError         if more than one exception is thrown by the proxied object
     * @see Method#invoke(Object, Object...)
     * @see Assert#fail(String)
     */
    @Override
    public Object mimic(T target, Method mimickedMethod, Object[] arguments) throws IllegalAccessException {

        try {
            returnValue = mimickedMethod.invoke(target, arguments);
            return returnValue;
        } catch (InvocationTargetException e) {

            if (thrownException != null) {

                fail("UnderTest class threw more than one exception!");
            }

            thrownException = e.getCause();
            return null;
        }
    }

    /**
     * Validates that no exception was thrown.
     *
     * @throws Throwable rethrows {@link #thrownException} if not null
     */
    void requireNoUnexpectedExceptionWasThrown() throws Throwable {

        if (thrownException != null && throwableHasNotBeenValidated) {
            throw thrownException;
        }
    }

    /**
     * Constructs a new {@link Then} out of current call result.
     *
     * @return new {@link Then}
     */
    Then then() {

        return new Then(this, thrownException, returnValue);
    }

    /**
     * Callback method used by {@link Then} to set the {@link #throwableHasNotBeenValidated} to false.
     */
    void onThrowableValidatedSuccessfully() {

        throwableHasNotBeenValidated = false;
    }
}
