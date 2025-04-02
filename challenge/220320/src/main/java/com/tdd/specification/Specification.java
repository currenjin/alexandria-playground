package com.tdd.specification;

public interface Specification {
    boolean isSatisfy(Object object);

    static Specification and(Specification left, Specification right) {
        return left.and(right);
    }

    static Specification or(Specification left, Specification right) {
        return left.or(right);
    }

    static Specification not(Specification specification) {
        return specification.not();
    }

    default Specification and(Specification right) {
        return new AndSpecification(this, right);
    }

    default Specification or(Specification right) {
        return new OrSpecification(this, right);
    }

    default Specification not() {
        return new NotSpecification(this);
    }
}
