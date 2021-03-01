package ${packagePath}.specification;

import ${packagePath}.specification.enums.BooleanOperator;

public class Specifications {
    public static <T> SpecificationUtils<T> and() {
        return new SpecificationUtils<>(BooleanOperator.AND);
    }

    public static <T> SpecificationUtils<T> or() {
        return new SpecificationUtils<>(BooleanOperator.OR);
    }
}
