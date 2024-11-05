package com.tdd.domain.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostUpdaterTest {
    @Mock
    private Post post;

    @Mock
    private PostRepository repository;

    private PostUpdater sut;

    @BeforeEach
    void setUp() {
        sut = new PostUpdater(repository);
    }

    @Test
    void updateTitleTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(post));

        sut.updateTitleBy(1L, "UPDATED TITLE");

        verify(repository).save(post);
    }
}