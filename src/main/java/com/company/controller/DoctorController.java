package com.company.controller;

import com.company.repository.AppointmentRepository;
import com.company.repository.DoctorRepository;
import com.company.session.Session;
import com.company.util.ConsoleIO;

import java.time.LocalDate;

public class DoctorController {
    private final ConsoleIO io;
    private final Session session;
    private final DoctorRepository doctors = new DoctorRepository();
    private final AppointmentRepository appt = new AppointmentRepository();

    public DoctorController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void todaySchedule() {
        Integer doctorId = doctors.findDoctorIdByUserId(session.getCurrentUser().getId());
        if (doctorId == null) { io.println("Doctor profile not found."); return; }
        appt.findDoctorDay(doctorId, LocalDate.now()).forEach(a ->
            io.println(a.appointmentId + " | " + a.startAt + " | " + a.patientName + " | " + a.status)
        );
    }

    public void markDone() {
        io.println("Mark DONE — можно добавить через AppointmentRepository.updateStatus(id, DONE).");
        io.println("Сделаешь как отдельную фичу (2 строки).");
    }
}
