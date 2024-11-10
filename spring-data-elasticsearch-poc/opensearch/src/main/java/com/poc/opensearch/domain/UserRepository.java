package com.poc.opensearch.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User, String> {
    List<User> findAllByNameContaining(String name);
    List<User> findAllByPhoneNumberContaining(String phoneNumber);
}
