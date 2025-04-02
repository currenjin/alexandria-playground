package com.poc.elasticsearch.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserRepository extends ElasticsearchRepository<User, String> {
    Optional<User> findByName(String name);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
