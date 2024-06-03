package com.gpb.entity;

import lombok.ToString;

@ToString
public class User {
    private long id;

    public User(long id) {
        this.id = id;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
