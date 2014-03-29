package com.github.lpandzic.junit.bdd;

/**
 * @author Lovro Pandzic
 */
@FunctionalInterface
public interface ThrowableSupplier<T extends Exception> {

    void get() throws T;
}


