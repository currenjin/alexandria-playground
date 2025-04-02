package com.tdd.specification;

public class AndSpecification implements Specification {
    private Specification left;
    private Specification right;

    public AndSpecification(Specification left, Specification right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfy(Object object) {
        return left.isSatisfy(object) && right.isSatisfy(object);
    }
}
