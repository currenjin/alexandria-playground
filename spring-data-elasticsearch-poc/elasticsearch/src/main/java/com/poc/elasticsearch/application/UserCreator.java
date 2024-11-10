package com.poc.elasticsearch.application;

import com.poc.elasticsearch.domain.User;
import com.poc.elasticsearch.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCreator {

    private final UserRepository userRepository;

    public void create(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    public UserCreator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
    }
}
