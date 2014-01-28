package com.github.lpandzic.junit.bdd;

import java.lang.annotation.*;

/**
 * Annotates field that references class being tested.
 *
 * <p><strong>Note that only one class should be marked with this annotation per test class!</strong></p>
 *
 * @author Lovro Pandzic
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnderTest {
}
