package com.github.lpandzic.junit.bdd;

/**
 * Represents a supplier of a {@link Throwable}.
 *
 * @param <T> the type of {@link Throwable} supplied by this supplier
 *
 * @author Lovro Pandzic
 */
@FunctionalInterface
public interface ThrowableSupplier<T extends Throwable> {

    void get() throws T;
}


