package io.github.coenraadhuman.composite.object.field.check.test.objects;

import io.github.coenraadhuman.composite.object.field.check.FieldNullCheck;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NestedEvaluationClass {

    private Integer integer;
    private String string;

    @FieldNullCheck
    private NormalClass normalClass;

}
