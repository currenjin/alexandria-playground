package com.tdd.domain.user;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    public User() {
        this(null, null, null);
    }

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.active = true;
        this.createdAt = new Date();
    }

    public User(String firstName, String lastName, String emailAddress, String address, String detailAddress, String phoneNumber, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.active = true;
        this.createdAt = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private boolean active;

    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
