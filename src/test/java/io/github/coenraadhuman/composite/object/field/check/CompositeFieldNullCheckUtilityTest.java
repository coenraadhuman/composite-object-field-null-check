package io.github.coenraadhuman.composite.object.field.check;

import io.github.coenraadhuman.composite.object.field.check.test.objects.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompositeFieldNullCheckUtilityTest {

    @Test
    public void when_basicPositiveTest() {
        var object = RootWithSingle
                .builder()
                .integer(1)
                .string("something")
                // This should be overwritten with null.
                .normalClass(NormalClass.builder().build())
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNull(object.getNormalClass());
    }

    @Test
    public void when_basicNegativeTest() {
        var object = RootWithSingle
                .builder()
                .integer(1)
                .string("something")
                // This should be overwritten with null.
                .normalClass(NormalClass
                        .builder()
                        .integer(1)
                        .build())
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNotNull(object.getNormalClass());
        assertEquals(1, object.getNormalClass().getInteger());
    }

    @Test
    public void when_basicNegativeTwoTest() {
        var object = RootWithSingle
                .builder()
                .integer(1)
                .string("something")
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNull(object.getNormalClass());
    }

    @Test
    public void when_basicNestedPositiveTest() {
        var object = RootWithNested
                .builder()
                .integer(1)
                .string("something")
                .nestedEvaluationClass(NestedEvaluationClass
                        .builder()
                        .integer(1)
                        .string("something")
                        // This should be overwritten with null.
                        .normalClass(NormalClass.builder().build())
                        .build())
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNotNull(object.getNestedEvaluationClass());
        assertEquals(1, object.getNestedEvaluationClass().getInteger());
        assertEquals("something", object.getNestedEvaluationClass().getString());
        assertNull(object.getNestedEvaluationClass().getNormalClass());
    }

    @Test
    public void when_basicNestedNegativeTest() {
        var object = RootWithNested
                .builder()
                .integer(1)
                .string("something")
                // This should be overwritten with null.
                .nestedEvaluationClass(NestedEvaluationClass
                        .builder()
                        // This should be overwritten with null.
                        .normalClass(NormalClass.builder().build())
                        .build())
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNull(object.getNestedEvaluationClass());
    }

    @Test
    public void when_basicBadlyAnnotatedNegativeTest() {
        var object = RootWithWronglyAnnotatedField
                .builder()
                .integer(1)
                // This was wrongly marked.
                .string("something")
                // This should be overwritten with null.
                .normalClass(NormalClass
                        .builder()
                        .build())
                .build();

        CompositeFieldNullCheckUtility.validate(object);

        assertEquals(1, object.getInteger());
        assertEquals("something", object.getString());
        assertNull(object.getNormalClass());
    }

}
