package com.company.controller;

import com.company.repository.AppointmentRepository;
import com.company.repository.DoctorRepository;
import com.company.service.AppointmentService;
import com.company.session.Session;
import com.company.util.ConsoleIO;

import java.time.LocalDate;

public class DoctorController {
    private final ConsoleIO io;
    private final Session session;

    private final DoctorRepository doctors = new DoctorRepository();
    private final AppointmentRepository appointments = new AppointmentRepository();
    private final AppointmentService service = new AppointmentService();

    public DoctorController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void todaySchedule() {
        Integer doctorId = doctors.findDoctorIdByUserId(session.getCurrentUser().getId());
        if (doctorId == null) {
            io.println("Doctor profile not found.");
            return;
        }
        var list = appointments.findDoctorDay(doctorId, LocalDate.now());
        if (list.isEmpty()) {
            io.println("No appointments today.");
            return;
        }
        for (var a : list) {
            io.println(a.appointmentId + " | " + a.startAt + " | " + a.patientName + " | " + a.status);
        }
    }

    public void markDone() {
        try {
            int appointmentId = io.readInt("Appointment id: ");
            service.markDone(appointmentId, session.getCurrentUser());
            io.println("Done.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }
}
