package com.wemeet.core.event.consume;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이벤트/커맨드 핸들러 표시 (골든패스). 비즈 개발자는 이 애노테이션만 붙인다.
 * 이벤트 타입은 메소드 파라미터(DomainEvent record의 @EventContract)에서, 컨슈머 그룹은
 * 서비스 설정 platform.events.consumer-group 에서 자동으로 정해져 공통이 레지스트리에 등록한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
}
