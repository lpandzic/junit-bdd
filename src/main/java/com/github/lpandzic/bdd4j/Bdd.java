package com.github.lpandzic.bdd4j;

import java.util.Optional;

/**
 * @author Lovro Pandzic
 */
final class Bdd {

    /**
     * {@link ThreadLocal} exception thrown or {@link Optional#empty()}.
     */
    private static ThreadLocal<Optional<Throwable>> thrownException = new ThreadLocal<>().withInitial(Optional::empty);

    /**
     * Inserts the {@code throwable} into {@link #thrownException}.
     *
     * @param throwable to add
     */
    static void putThrownException(Throwable throwable) {

        requireThatNoUnexpectedExceptionWasThrown();

        thrownException.set(Optional.of(throwable));
    }

    /**
     * Retrieves and removes {@link #thrownException}.
     *
     * Used by {@link Then} for consuming {@link #thrownException}
     *
     * @return {@link #thrownException}
     */
    static Optional<Throwable> takeThrownException() {

        Optional<Throwable> thrownException = Bdd.thrownException.get();

        Bdd.thrownException.set(Optional.<Throwable>empty());

        return thrownException;
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void throwUnexpectedException(Optional<Throwable> throwable) throws T {

        if (throwable.isPresent()) {
            throw (T) throwable.get();
        }
    }

    /**
     * Throws {@link #thrownException} if present.
     *
     * @throws IllegalStateException if a {@code thrownException} already contains an exception,
     *                               the previous thrown exception is wrapped
     */
    static void requireThatNoUnexpectedExceptionWasThrown() {

        if (thrownException.get().isPresent()) {
            throwUnexpectedException(takeThrownException());
        }
    }

    private Bdd() {

    }
}
