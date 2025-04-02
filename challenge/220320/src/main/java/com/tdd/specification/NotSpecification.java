package com.tdd.specification;

public class NotSpecification implements Specification {
    private Specification specification;

    public NotSpecification(Specification specification) {
        this.specification = specification;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return !specification.isSatisfy(object);
    }
}
