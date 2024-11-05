package com.currenjin.learningtest.mockedstatic;

public class User {

    private final String name;

    public User(String name) {
        this.name = name;
    }

    public static User superUserOf() {
        return new User("super");
    }

    public String getName() {
        return this.name;
    }
}
