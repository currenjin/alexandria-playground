package com.currenjin.learningtest.core.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class UserService implements InitializingBean, DisposableBean {
    @Value("${user.cache.enabled:true}")
    private boolean cacheEnabled;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        System.out.println("1. 생성자 호출: Bean 인스턴스 생성. property : " + cacheEnabled);
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("3. PostConstruct 메서드 실행: UserService. property : " + cacheEnabled);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. afterPropertiesSet 메서드 실행: UserService. property : " + cacheEnabled);
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("5. PreDestroy 메서드 실행: UserService. property : " + cacheEnabled);
    }

    @Override
    public void destroy() {
        System.out.println("6. destroy 메서드 실행: UserService. property : " + cacheEnabled);
    }
}
