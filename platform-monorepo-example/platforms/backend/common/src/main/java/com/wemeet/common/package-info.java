/**
 * 순수 유틸 모듈(placeholder). 도메인·프레임워크(Spring·Kafka 등)에 의존하지 않는
 * 범용 유틸리티만 둔다. 어떤 모듈에서도 안전하게 의존할 수 있어야 하므로 의존은 최소로 유지한다.
 *
 * <p>주의: 이벤트 계약은 {@code platforms/contract}, 백본 인프라는 {@code platforms/core}에 둔다.
 * 여기(commons)에 이벤트·프레임워크 코드를 넣지 말 것(경계 오염).
 */
package com.wemeet.common;
