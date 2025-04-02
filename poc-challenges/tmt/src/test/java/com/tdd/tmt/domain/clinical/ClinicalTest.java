package com.tdd.tmt.domain.clinical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/* 임상시험 자격 요건

만 19세 이상 55세 이하의 건강한 성인 남성 또는 여성

자발적으로 임상시험 참여를 결정하고 주의사항 등을 준수하기로 서면동의 가능하신 분

모집 인원: 36명

*/

class ClinicalTest {

    private static Clinical clinical;
    private static Person person;

    @BeforeEach
    void setUp() {
        clinical = new Clinical();
        person = new Person(20, true);
    }

    @Test
    void condition_true() {
        assertThat(clinical.condition(person.getAge(), person.isAgreed())).isTrue();
    }

    @Test
    void condition_false_young() {
        person.setAge(15);
        assertThat(clinical.condition(person.getAge(), person.isAgreed())).isFalse();
    }

    @Test
    void condition_false_old() {
        person.setAge(56);
        assertThat(clinical.condition(person.getAge(), person.isAgreed())).isFalse();
    }

    @Test
    void condition_false_not_agreed() {
        person.setAgreed(false);
        assertThat(clinical.condition(person.getAge(), person.isAgreed())).isFalse();
    }

    @Test
    void condition_over_people() {
        clinical.setMaximumPeople(36);
        clinical.setCurrentPeople(36);
        assertThat(clinical.condition(person.getAge(), person.isAgreed())).isFalse();
    }
}