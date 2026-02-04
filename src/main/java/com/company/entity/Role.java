package com.company.entity;

public enum Role {
    PATIENT, DOCTOR, ADMIN;

    public static Role fromDb(String s) {
        return Role.valueOf(s.trim().toUpperCase());
    }
}
