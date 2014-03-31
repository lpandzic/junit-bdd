package com.github.lpandzic.junit.bdd;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Lovro Pandzic
 */
public final class Then {

    public static final class Throws<T extends Exception> {

        private final Optional<T> exception;

        public Throws(Optional<T> exception) {

            this.exception = Objects.requireNonNull(exception);
        }

        @SuppressWarnings("unchecked")
        public <R extends Throwable> void then(Consumer<R> consumer) {

            consumer.accept((R) exception.orElse(null));
        }

        @SuppressWarnings("unchecked")
        public <E extends T> void thenChecked(Consumer<E> consumer) {

            consumer.accept((E) exception.orElse(null));
        }
    }

    public static final class Returns<T> {

        private final Optional<T> value;

        public Returns(Optional<T> value) {

            this.value = value;
        }

        public void then(Consumer<T> consumer) {

            consumer.accept(value.orElse(null));
        }
    }
}