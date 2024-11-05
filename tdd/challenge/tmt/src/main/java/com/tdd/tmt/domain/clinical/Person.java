package com.tdd.tmt.domain.clinical;

public class Person {
    private int age;
    private boolean isAgreed;

    public Person(int age, boolean isAgreed) {
        this.age = age;
        this.isAgreed = isAgreed;
    }

    public Person() {
    }

    public int getAge() {
        return age;
    }

    public boolean isAgreed() {
        return isAgreed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAgreed(boolean agreed) {
        isAgreed = agreed;
    }
}
