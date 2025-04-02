package com.poc.opensearch.presentation;

public class UserPostRequest {
    private final String id;
    private final String name;
    private final String phoneNumber;

    public UserPostRequest(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
