package com.company.entity;

public enum AppointmentStatus {
    BOOKED, CANCELLED, RESCHEDULED, DONE;

    public static AppointmentStatus fromString(String s) {
        return AppointmentStatus.valueOf(s.trim().toUpperCase());
    }
}


