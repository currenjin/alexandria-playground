package com.currenjin.internalapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    public Long findPostById(Long postId) {
        return postId;
    }
}
