package io.github.coenraadhuman.composite.object.field.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompositeFieldNullCheckUtility {

    public static void validate(Object object) {
        if (Objects.nonNull(object)) {
            var annotatedFields = getNullCheckAnnotatedFields(object);

            for (var annotatedField : annotatedFields) {
                evaluateAnnotatedField(object, annotatedField);
            }
        }
    }

    @SneakyThrows
    private static boolean evaluateAnnotatedField(Object rootObject, Field field) {
        if (isValidAnnotatedField(field.getType())) {
            field.setAccessible(true);
            var object = field.get(rootObject);

            if (Objects.nonNull(object)) {
                var annotatedFields = getNullCheckAnnotatedFields(object);
                var otherFields = getOtherFields(object);

                var allAnnotatedFieldsAreNull = true;

                for (var annotatedField : annotatedFields) {
                    if (evaluateAnnotatedField(object, annotatedField)) {
                        allAnnotatedFieldsAreNull = false;
                    }
                }

                var allOtherFieldsAreNull = isOtherFieldsNull(object, otherFields);

                if (allAnnotatedFieldsAreNull && allOtherFieldsAreNull) {
                    field.set(rootObject, null);
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidAnnotatedField(Class<?> type) {
        var isInvalid = type.equals(Boolean.class)
                || type.equals(Byte.class)
                || type.equals(Character.class)
                || type.equals(Double.class)
                || type.equals(Exception.class)
                || type.equals(Float.class)
                || type.equals(Integer.class)
                || type.equals(Long.class)
                || type.equals(Short.class)
                || type.equals(String.class)
                || type.isPrimitive()
                || type.isAnnotation()
                || type.isArray()
                || type.isEnum()
                || type.isInterface()
                || type.isSynthetic();

        return !isInvalid;
    }

    private static boolean isOtherFieldsNull(Object rootObject, List<Field> fields) {
        if (fields.isEmpty()) {
            return true;
        } else {
            return fields
                    .stream()
                    .noneMatch(x -> {
                        try {
                            x.setAccessible(true);
                            return Objects.nonNull(x.get(rootObject));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return true;
                    });
        }
    }

    private static List<Field> getNullCheckAnnotatedFields(Object object) {
        var fields =  object.getClass().getDeclaredFields();

        return Arrays
                .stream(fields)
                .filter(x -> Objects.nonNull(x.getAnnotation(FieldNullCheck.class)))
                .collect(Collectors.toList());
    }

    private static List<Field> getOtherFields(Object object) {
        var fields =  object.getClass().getDeclaredFields();

        return Arrays
                .stream(fields)
                .filter(x -> Objects.isNull(x.getAnnotation(FieldNullCheck.class)))
                .collect(Collectors.toList());
    }

}
