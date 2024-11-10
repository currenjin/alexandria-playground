package com.poc.opensearch.application;

import com.poc.opensearch.domain.User;
import com.poc.opensearch.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<User> findAllByName(String name) {
        return userRepository.findAllByNameContaining(name);
    }

    public List<User> findAllByPhoneNumber(String phoneNumber) {
        return userRepository.findAllByPhoneNumberContaining(phoneNumber);
    }
}
