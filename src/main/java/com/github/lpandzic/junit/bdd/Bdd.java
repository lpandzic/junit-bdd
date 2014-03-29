package com.github.lpandzic.junit.bdd;

import java.util.Objects;
import java.util.Optional;

/**
 *
 *
 * @author Lovro Pandzic
 */
public class Bdd {

    @SuppressWarnings("unchecked")
    public static <T extends Exception> Then.Throws<T> when(ThrowableSupplier<T> throwableSupplier) {

        Objects.requireNonNull(throwableSupplier);

        try {
            throwableSupplier.get();
        } catch (Exception t) {
            return new Then.Throws<>(Optional.of((T) t));
        }

        return new Then.Throws<>(Optional.empty());
    }

    public static <T> Then.Returns<T> when(T value) {

        return new Then.Returns<>(Optional.ofNullable(value));
    }
}
