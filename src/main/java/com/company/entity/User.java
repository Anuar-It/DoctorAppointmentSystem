package com.company.entity;

public class User {
    private int id;
    private String name;
    private String role;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
