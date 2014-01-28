package com.github.lpandzic.junit.bdd;

/**
 * Thrown to indicate an improper usage of the {@link Bdd}.
 *
 * @author Lovro Pandzic
 * @see BddJunit
 * @see Bdd
 * @see Then
 */
public final class BddException extends RuntimeException {

    BddException(String message) {

        super(message);
    }
}
