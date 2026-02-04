package com.company.entity;

public class Doctor {
    private final int id;
    private final int userId;
    private final String doctorName;
    private final String specialization;
    private final String cabinet;
    private final boolean active;

    public Doctor(int id, int userId, String doctorName, String specialization, String cabinet, boolean active) {
        this.id = id;
        this.userId = userId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.cabinet = cabinet;
        this.active = active;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialization() { return specialization; }
    public String getCabinet() { return cabinet; }
    public boolean isActive() { return active; }
}
