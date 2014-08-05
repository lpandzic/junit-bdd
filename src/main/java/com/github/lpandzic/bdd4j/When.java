package com.github.lpandzic.bdd4j;

import java.util.Optional;

/**
 * Defines behavior that is being tested.
 *
 * @author Lovro Pandzic
 */
public final class When {

    When() {
    }

    /**
     * Used for specifying behavior that should throw an exception.
     *
     * @param throwableSupplier supplier or throwable
     * @param <T>               type of throwable
     *
     * @return new {@link Then.Throws}
     */
    public <T extends Throwable> Then.Throws<T> when(ThrowableSupplier<T> throwableSupplier) {

        try {
            throwableSupplier.get();
        } catch (Throwable t) {
            Bdd.putThrownException(t);
        }

        return new Then.Throws<>();
    }

    /**
     * Used for specifying behavior that should return a value.
     *
     * @param value returned by the specified behavior
     * @param <T>   type of {@code value}
     *
     * @return new {@link Then.Returns}
     */
    public <T> Then.Returns<T> when(T value) {

        return new Then.Returns<>(Optional.ofNullable(value));
    }
}
