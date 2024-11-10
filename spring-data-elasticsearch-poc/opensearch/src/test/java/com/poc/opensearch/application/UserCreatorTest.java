package com.poc.opensearch.application;

import com.poc.opensearch.domain.User;
import com.poc.opensearch.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreatorTest {
    User user = new User("S", "S", "S");

    @Mock
    UserRepository repository;

    UserCreator sut;

    @BeforeEach
    void setUp() {
        sut = new UserCreator(repository);
    }

    @Test
    void 유저를_생성합니다() {
        sut.create(user);

        verify(repository).save(user);
    }

    @Test
    void 유효하지_않은_유저를_전달하면_예외가_발생합니다() {
        assertThatThrownBy(() -> sut.create(null)).isInstanceOf(IllegalArgumentException.class);
    }
}