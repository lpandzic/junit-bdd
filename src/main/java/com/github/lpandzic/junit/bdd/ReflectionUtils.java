package com.github.lpandzic.junit.bdd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection utilities used for manipulating {@link UnderTest} field in the test class.
 *
 * @author Lovro Pandzic
 * @see UnderTest
 * @see Bdd
 */
final class ReflectionUtils {

    static List<Field> findAnnotatedFields(Object testCase, Class<? extends Annotation> annotationClass) {

        final Field[] fields = testCase.getClass().getDeclaredFields();
        List<Field> matchedFields = new ArrayList<Field>();

        for (Field field : fields) {
            if (field.getAnnotation(annotationClass) != null) {
                matchedFields.add(field);
            }
        }

        return matchedFields;
    }

    static Object getFieldValue(Field field, Object testObject) {

        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        try {
            return field.get(testObject);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    static void setFieldValue(Field field, Object obj, Object value) {

        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        field.setAccessible(isAccessible);
    }

    private ReflectionUtils() {

    }
}
