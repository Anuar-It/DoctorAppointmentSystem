package com.company.controller;

import com.company.db.DBConnection;
import com.company.entity.Role;
import com.company.repository.DoctorRepository;
import com.company.repository.UserRepository;
import com.company.service.AppointmentService;
import com.company.session.Session;
import com.company.util.ConsoleIO;
import com.company.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminController {
    private final ConsoleIO io;
    private final Session session;

    private final AppointmentService ap = new AppointmentService();
    private final DoctorRepository doctors = new DoctorRepository();
    private final UserRepository users = new UserRepository();

    public AdminController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void addSpecialization() {
        try {
            String name = io.readLine("Specialization name: ");
            String sql = "INSERT INTO specializations(name) VALUES(?)";
            try (Connection c = DBConnection.getInstance().getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
            io.println("Added.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void addDoctor() {
        try {
            String fullName = io.readLine("Doctor full name: ");
            String username = io.readLine("Doctor username: ");
            String password = io.readLine("Doctor password: ");
            int specId = io.readInt("Specialization id: ");
            String cabinet = io.readLine("Cabinet: ");

            if (users.findByUsername(username).isPresent()) {
                io.println("Username already exists.");
                return;
            }

            int userId = users.insert(fullName, username, PasswordHasher.sha256(password), Role.DOCTOR).getId();
            doctors.insertDoctor(userId, specId, cabinet);
            io.println("Doctor created.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void deactivateDoctor() {
        try {
            int doctorId = io.readInt("Doctor id: ");
            doctors.deactivate(doctorId);
            io.println("Deactivated.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void viewAllFull() {
        var list = ap.allAppointments();
        if (list.isEmpty()) {
            io.println("No appointments.");
            return;
        }
        for (var a : list) {
            io.println(a.appointmentId + " | " + a.startAt + " | " + a.status + " | " + a.patientName + " -> " + a.doctorName);
        }
    }
}
