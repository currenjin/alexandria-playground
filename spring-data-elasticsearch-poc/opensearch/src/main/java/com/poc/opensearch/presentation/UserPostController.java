package com.poc.opensearch.presentation;

import com.poc.opensearch.application.UserCreator;
import com.poc.opensearch.domain.User;
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
