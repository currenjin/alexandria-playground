package com.wemeet.core.event.contract;

import java.lang.annotation.*;

/**
 * 이벤트/커맨드의 논리 타입명·버전 선언. FQCN 금지 — 논리명 사용.
 * 엔진이 이 값으로 봉투 eventType을 채우고, 소비 측 역직렬화·토픽 유도에 쓴다.
 * 계약 메커니즘(라이브러리) — 예제 계약(contracts 모듈)이 이 애노테이션을 붙인다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventContract {
    String type();
    int version() default 1;
}
