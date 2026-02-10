package com.currenjin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 255)
    private String email;

    private int age;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Member(String name, String email, int age, String status) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
