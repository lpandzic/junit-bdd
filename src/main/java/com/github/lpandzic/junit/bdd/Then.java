package com.github.lpandzic.junit.bdd;

import org.hamcrest.Matcher;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

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
        public MessageOrCause thenShouldThrowExceptionThat(Matcher<? extends Exception> matcher) {

            Objects.requireNonNull(matcher);
            assertThat(exception.orElse(null), (Matcher<? super T>) matcher);
            return new MessageOrCause();
        }

        @SuppressWarnings("unchecked")
        public MessageOrCause thenShouldThrowCheckedExceptionThat(Matcher<? extends T> matcher) {

            Objects.requireNonNull(matcher);
            assertThat(exception.orElse(null), (Matcher<? super T>) matcher);
            return new MessageOrCause();
        }

        @SuppressWarnings("unchecked")
        private void verifyCause(Matcher<? extends Throwable> matcher) {

            Objects.requireNonNull(matcher);
            assertThat(exception.map(T::getCause).orElse(null), (Matcher<? super Throwable>) matcher);
        }

        private void verifyMessage(Matcher<String> matcher) {

            Objects.requireNonNull(matcher);
            assertThat(exception.map(T::getMessage).orElse(null), matcher);
        }

        public final class MessageOrCause {

            public CauseEnding withMessageThat(Matcher<String> matcher) {

                verifyMessage(matcher);
                return new CauseEnding();
            }


            public MessageEnding withCauseThat(Matcher<? extends Throwable> matcher) {

                verifyCause(matcher);
                return new MessageEnding();
            }

            private MessageOrCause() {

            }
        }

        public final class CauseEnding {

            public void andCauseThat(Matcher<? extends Throwable> matcher) {

                verifyCause(matcher);
            }

            private CauseEnding() {

            }
        }

        public final class MessageEnding {

            public void andMessageThat(Matcher<String> matcher) {

                verifyMessage(matcher);
            }

            private MessageEnding() {

            }
        }
    }

    public static final class Returns<T> {

        private final Optional<T> value;

        public Returns(Optional<T> value) {

            this.value = value;
        }

        public void thenShouldReturnValueThat(Matcher<T> matcher) {

            Objects.requireNonNull(matcher);
            assertThat(value.orElse(null), matcher);
        }
    }
}