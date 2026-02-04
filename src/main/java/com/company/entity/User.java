package com.company.entity;

import java.time.LocalDateTime;

public class User {
    private final int id;
    private final String fullName;
    private final String username;
    private final String passwordHash;
    private final Role role;
    private final LocalDateTime createdAt;

    public User(int id, String fullName, String username, String passwordHash, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
