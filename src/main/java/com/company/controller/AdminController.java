package com.company.controller;

import com.company.repository.AppointmentRepository;
import com.company.service.AppointmentService;
import com.company.session.Session;
import com.company.util.ConsoleIO;

public class AdminController {
    private final ConsoleIO io;
    private final Session session;
    private final AppointmentService service = new AppointmentService();

    public AdminController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void addSpecialization() {
        io.println("Добавить specialization — делается отдельным SpecializationRepository (2 SQL запроса).");
    }

    public void addDoctor() {
        io.println("Add doctor — обычно: создать user(role=DOCTOR) + вставить в doctors(user_id, specialization_id...).");
    }

    public void deactivateDoctor() {
        io.println("Deactivate doctor — UPDATE doctors SET active=false WHERE id=?");
    }

    public void viewAllFull() {
        service.allFull().forEach(a ->
            io.println(a.appointmentId + " | " + a.startAt + " | " + a.status + " | " + a.patientName + " -> " + a.doctorName)
        );
    }
}
