package com.tdd.domain.comment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    void create() {
        Comment comment = new Comment();

        assertInstanceOf(Comment.class, comment);
    }

    @Test
    void get() {
        Comment comment = new Comment(1L, "CONTENT");

        assertEquals(1L, comment.getUserId());
        assertEquals("CONTENT", comment.getContent());
    }

    @Test
    void set() {
        Comment comment = new Comment(1L, "CONTENT");
        assertEquals(1L, comment.getUserId());
        assertEquals("CONTENT", comment.getContent());

        comment.setUserId(2L);
        comment.setContent("TNETNOC");

        assertEquals(2L, comment.getUserId());
        assertEquals("TNETNOC", comment.getContent());
    }
}