package com.tdd.domain.post;

import com.tdd.domain.comment.Comment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PostTest {

    @Test
    void create() {
        Post post = new Post();

        assertInstanceOf(Post.class, post);
    }

    @Test
    void get() {
        Post post = new Post("TITLE", "CONTENT");

        assertEquals("TITLE", post.getTitle());
        assertEquals("CONTENT", post.getContent());
        assertEquals(0, post.getComments().size());
    }

    @Test
    void set() {
        Post post = new Post("TITLE", "CONTENT");
        assertEquals("TITLE", post.getTitle());
        assertEquals("CONTENT", post.getContent());

        post.setTitle("ELTIT");
        post.setContent("TNETNOC");

        assertEquals("ELTIT", post.getTitle());
        assertEquals("TNETNOC", post.getContent());
    }

    @Test
    void add_comment() {
        Post post = new Post("TITLE", "CONTENT");
        Comment comment = new Comment();
        assertEquals(0, post.getComments().size());

        post.addComment(comment);

        assertEquals(1, post.getComments().size());
    }
}