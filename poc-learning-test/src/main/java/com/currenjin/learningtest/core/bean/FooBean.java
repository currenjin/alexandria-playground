package com.currenjin.learningtest.core.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class FooBean implements InitializingBean, DisposableBean {

    public FooBean() {
        System.out.println("1. 생성자 호출: Bean 인스턴스 생성");
    }

    @PostConstruct
    public void init() {
        System.out.println("3. @PostConstruct: 의존성 주입이 완료된 후 호출");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. InitializingBean's afterPropertiesSet: 초기화");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("5. @PreDestroy: Bean 소멸 전 호출");
    }

    @Override
    public void destroy() {
        System.out.println("6. DisposableBean's destroy: Bean 소멸");
    }
}
