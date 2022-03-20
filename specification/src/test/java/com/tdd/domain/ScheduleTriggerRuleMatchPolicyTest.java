package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ScheduleTriggerRuleMatchPolicyTest {

    Object object = new Object();

    ScheduleTriggerRuleMatchCondition condition = mock(ScheduleTriggerRuleMatchCondition.class);
    ScheduleTriggerRule rule = new ScheduleTriggerRule();
    ScheduleTriggerRuleMatchPolicy sut = new ScheduleTriggerRuleMatchPolicy(condition, rule);

    @Test
    void 조건이_만족하지_않으면() {
        given(condition.isSatisfy(object)).willReturn(false);
        assertThat(sut.createScheduleGroup(object).isPresent()).isFalse();
    }

    @Test
    void 조건이_만족하면() {
        given(condition.isSatisfy(object)).willReturn(true);
        assertThat(sut.createScheduleGroup(object).isPresent()).isTrue();
    }
}