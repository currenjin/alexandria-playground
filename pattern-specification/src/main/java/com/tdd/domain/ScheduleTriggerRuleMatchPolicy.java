package com.tdd.domain;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ScheduleTriggerRuleMatchPolicy {
    private final ScheduleTriggerRuleMatchCondition condition;
    private final ScheduleTriggerRule rule;

    public Optional<ScheduleGroup> createScheduleGroup(Object object) {
        if (condition.isSatisfy(object)) {
            return Optional.ofNullable(rule.createScheduleGroup(object));
        }

        return Optional.empty();
    }
}
