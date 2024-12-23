package com.currenjin.internalapi.controller;

import com.currenjin.internalapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{postId}")
    public Long findPostById(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }
}
