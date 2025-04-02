package com.tdd.domain;

import com.tdd.util.spec.Condition;

public interface ScheduleTriggerRuleMatchCondition extends Condition {

    boolean isSatisfy(Object object);
}
