package com.poc.opensearch.presentation;

import com.poc.opensearch.application.UserFinder;
import com.poc.opensearch.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserGetController {

    private final UserFinder userFinder;

    public UserGetController(UserFinder userFinder) {
        this.userFinder = userFinder;
    }

    @GetMapping(value = "/{userId}")
    public UserResponse getUserById(@PathVariable String userId) {
        User user = userFinder.findById(userId);
        return convertToUserResponseBy(user);
    }

    @GetMapping(params = "name")
    public List<UserResponse> getUserByName(@RequestParam(value = "name") String name) {
        List<User> users = userFinder.findAllByName(name);
        return convertToUserResponseBy(users);
    }

    @GetMapping(params = "phoneNumber")
    public List<UserResponse> getUserByPhoneNumber(@RequestParam(value = "phoneNumber") String phoneNumber) {
        List<User> users = userFinder.findAllByPhoneNumber(phoneNumber);
        return convertToUserResponseBy(users);
    }

    private UserResponse convertToUserResponseBy(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getPhoneNumber());
    }

    private List<UserResponse> convertToUserResponseBy(List<User> users) {
        List<UserResponse> userResponseList = new ArrayList<>();

        users.forEach(e -> userResponseList.add(new UserResponse(e.getId(), e.getName(), e.getPhoneNumber())));

        return userResponseList;
    }
}
