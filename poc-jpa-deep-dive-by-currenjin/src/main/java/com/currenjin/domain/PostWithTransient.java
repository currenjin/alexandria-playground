package com.currenjin.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class PostWithTransient {
    @Id
    private Long id;

    private String title;

    @Transient
    private String transientTitle;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTransientTitle() { return transientTitle; }
    public void setTransientTitle(String transientTitle) { this.transientTitle = transientTitle; }
}
