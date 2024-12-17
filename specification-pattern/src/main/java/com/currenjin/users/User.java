package com.currenjin.users;

public class User {
    private final String name;
    private final int age;

    public User(String name, int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age must be a positive integer");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
