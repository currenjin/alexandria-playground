package com.tdd.specification;

public class OrSpecification implements Specification {
    private Specification left;
    private Specification right;

    public OrSpecification(Specification left, Specification right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return this.left.isSatisfy(object) || this.right.isSatisfy(object);
    }
}
