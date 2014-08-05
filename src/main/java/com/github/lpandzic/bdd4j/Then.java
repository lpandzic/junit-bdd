package com.github.lpandzic.bdd4j;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Defines expected outcomes produced by {@link When}.
 *
 * @author Lovro Pandzic
 */
public final class Then {

    /**
     * Used to describe expected thrown exception.
     *
     * @param <T> type of expected {@link Throwable}
     */
    public static final class Throws<T extends Throwable> {

        Throws() {
        }

        public void then(Consumer<Throwable> consumer) {

            Optional<Throwable> throwable = Bdd.takeThrownException();
            consumer.accept(throwable.orElse(null));
        }

        @SuppressWarnings("unchecked")
        public <E extends T> void thenChecked(Consumer<E> consumer) {

            Optional<Throwable> throwable = Bdd.takeThrownException();

            try {
                consumer.accept((E) throwable.orElse(null));
            }  catch (ClassCastException e) {
                Bdd.throwUnexpectedException(throwable);
            }
        }

        public void thenShouldNotThrow() {
            Bdd.requireThatNoUnexpectedExceptionWasThrown();
        }
    }

    /**
     * Used to describe expected returned value.
     *
     * @param <T> type of returned value
     */
    public static final class Returns<T> {

        private final Optional<T> value;

        Returns(Optional<T> value) {

            this.value = value;
        }

        public void then(Consumer<T> consumer) {

            consumer.accept(value.orElse(null));
        }
    }
}