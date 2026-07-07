package com.wemeet.eventbackbone.contracts;

import java.lang.annotation.*;

/**
 * 이벤트/커맨드의 논리 타입명·버전 선언 (§7.1.1). FQCN 금지 — 논리명 사용.
 * 엔진이 이 값으로 봉투 eventType을 채우고, 소비 측 역직렬화·토픽 유도(§7.1.2)에 쓴다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventContract {
    String type();          // 예: "oms.order.confirmed"
    int version() default 1;
}
