package com.tdd.domain.consumer;

public class Signup {
    private final String email;
    private final String password;

    public Signup(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
