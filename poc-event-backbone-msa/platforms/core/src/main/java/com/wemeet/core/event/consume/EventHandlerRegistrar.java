package com.wemeet.core.event.consume;

import com.wemeet.core.event.contract.DomainEvent;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @EventHandler 자동 등록. 모든 빈을 훑어 @EventHandler 메소드를 찾아, 파라미터 타입에서
 * 이벤트 타입을 유도하고 서비스의 컨슈머 그룹으로 HandlerRegistry에 등록한다.
 * 비즈 개발자가 registry.register/@PostConstruct 보일러플레이트를 쓰지 않게 한다.
 */
@Component
public class EventHandlerRegistrar implements BeanPostProcessor {

    private final HandlerRegistry registry;
    private final String group;

    public EventHandlerRegistrar(HandlerRegistry registry,
                                 @Value("${platform.events.consumer-group:}") String group) {
        this.registry = registry;
        this.group = group;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> target = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(target, method -> {
            if (!method.isAnnotationPresent(EventHandler.class)) return;
            if (method.getParameterCount() != 1 || !DomainEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                throw new IllegalStateException("@EventHandler는 DomainEvent 파라미터 하나여야 합니다: " + method);
            }
            ReflectionUtils.makeAccessible(method);
            Class<? extends DomainEvent> eventType = (Class<? extends DomainEvent>) method.getParameterTypes()[0];
            registry.register(group, eventType, event -> invoke(method, bean, event));
        });
        return bean;
    }

    private void invoke(Method method, Object bean, DomainEvent event) {
        try {
            method.invoke(bean, event);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) throw re;
            throw new RuntimeException(cause);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
