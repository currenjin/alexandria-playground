package com.wemeet.eventbackbone;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이벤트 백본 활성화 마스터 스위치 (§7.1.9). 이 애노테이션을 붙인 앱에만 백본 공통 빈
 * (봉투·발행자·릴레이·소비지원·Outbox/Inbox·컨텍스트·사가엔진·핸들러 자동등록)이 올라온다.
 * 안 붙이면 platform-core를 의존해도 아무것도 켜지지 않는다 — 의존성만으로 몰래 켜지는 starter 방식을 배제한다.
 * (소비는 한 단계 더 명시: subscribe-topics 선언 + 모듈 리스너가 있어야 컨슈머가 생긴다.)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EventBackboneConfig.class)
public @interface EnableEventBackbone {
}
