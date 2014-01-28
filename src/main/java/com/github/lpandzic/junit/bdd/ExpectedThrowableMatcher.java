package com.github.lpandzic.junit.bdd;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.matchers.JUnitMatchers.isThrowable;

/**
 * @see org.junit.rules.ExpectedExceptionMatcherBuilder
 */
class ExpectedThrowableMatcher {

    private final List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();

    void add(Matcher<?> matcher) {
        matchers.add(matcher);
    }

    Matcher<Throwable> build() {
        return isThrowable(allOfTheMatchers());
    }

    private Matcher<Throwable> allOfTheMatchers() {
        if (matchers.size() == 1) {
            return cast(matchers.get(0));
        }
        return allOf(castedMatchers());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Matcher<? super Throwable>> castedMatchers() {
        return new ArrayList<Matcher<? super Throwable>>((List) matchers);
    }

    @SuppressWarnings("unchecked")
    private Matcher<Throwable> cast(Matcher<?> singleMatcher) {
        return (Matcher<Throwable>) singleMatcher;
    }
}
