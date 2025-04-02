package com.currenjin.learningtest.dirtiescontext;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserCache {

    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        userList.add(user);
    }
}
