package com.poc.elasticsearch.application;

import com.poc.elasticsearch.domain.User;
import com.poc.elasticsearch.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserFinder {

    private final UserRepository userRepository;

    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(IllegalArgumentException::new);
    }
}
