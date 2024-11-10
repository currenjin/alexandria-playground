package com.poc.elasticsearch.application;

import com.poc.elasticsearch.domain.User;
import com.poc.elasticsearch.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserFinderTest {

    private static final String ANY_STRING = "String";

    @Mock
    UserRepository repository;

    @Mock
    private User user;

    UserFinder sut;

    @BeforeEach
    void setUp() {
        sut = new UserFinder(repository);
    }

    @Test
    void ID_로_조회_시_유저가_없으면_예외가_발생한다() {
        given(repository.findById(ANY_STRING)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(ANY_STRING)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void NAME_으로_조회_시_유저가_없으면_예외가_발생한다() {
        given(repository.findByName(ANY_STRING)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findByName(ANY_STRING)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void PHONE_NUMBER_로_조회_시_유저가_없으면_예외가_발생한다() {
        given(repository.findByPhoneNumber(ANY_STRING)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findByPhoneNumber(ANY_STRING)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ID_로_조회한다() {
        given(repository.findById(ANY_STRING)).willReturn(Optional.of(user));

        sut.findById(ANY_STRING);

        verify(repository).findById(ANY_STRING);
    }

    @Test
    void NAME_으로_조회한다() {
        given(repository.findByName(ANY_STRING)).willReturn(Optional.of(user));

        sut.findByName(ANY_STRING);

        verify(repository).findByName(ANY_STRING);
    }

    @Test
    void PHONE_NUMBER_로_조회한다() {
        given(repository.findByPhoneNumber(ANY_STRING)).willReturn(Optional.of(user));

        sut.findByPhoneNumber(ANY_STRING);

        verify(repository).findByPhoneNumber(ANY_STRING);
    }
}