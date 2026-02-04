package com.company.service;

import java.time.LocalDateTime;

public class ValidationService {

    public void notBlank(String v, String field) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(field + " is required");
    }

    public void passwordRules(String password) {
        if (password.length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters");
    }

    public void notPast(LocalDateTime dt) {
        if (dt.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Date/time cannot be in the past");
    }
}
