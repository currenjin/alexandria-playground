package com.poc.elasticsearch.presentation;

import com.poc.elasticsearch.application.UserCreator;
import com.poc.elasticsearch.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserPostController {

    private final UserCreator userCreator;

    public UserPostController(UserCreator userCreator) {
        this.userCreator = userCreator;
    }

    @PostMapping
    public void postUser(@RequestBody UserPostRequest request) {
        User user = new User(request.getId(), request.getName(), request.getPhoneNumber());
        userCreator.create(user);
    }

}
