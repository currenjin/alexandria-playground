package com.currenjin.learningtest.core.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository
public class UserRepository implements InitializingBean, DisposableBean {
    public UserRepository() {
        System.out.println("2. 의존성 주입: Properties 세팅");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("3. PostConstruct 메서드 실행: UserRepository");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. afterPropertiesSet 메서드 실행: UserRepository");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("5. PreDestroy 메서드 실행: UserRepository");
    }

    @Override
    public void destroy() {
        System.out.println("6. destroy 메서드 실행: UserRepository");
    }
}
