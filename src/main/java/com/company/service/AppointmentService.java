package com.company.service;

import com.company.dto.FullAppointmentDto;
import com.company.entity.AppointmentStatus;
import com.company.entity.Role;
import com.company.entity.User;
import com.company.repository.AppointmentHistoryRepository;
import com.company.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final AppointmentRepository repo = new AppointmentRepository();
    private final AppointmentHistoryRepository hist = new AppointmentHistoryRepository();
    private final ValidationService val = new ValidationService();

    public int book(User actor, int doctorId, LocalDateTime startAt, String reason) {
        if (actor.getRole() != Role.PATIENT) throw new IllegalArgumentException("Only PATIENT can book");
        val.notPast(startAt);
        if (repo.slotTaken(doctorId, startAt)) throw new IllegalArgumentException("Slot is taken");

        int id = repo.insert(doctorId, actor.getId(), startAt, reason);
        hist.log(id, "BOOK", null, startAt, actor.getId(), "created");
        return id;
    }

    public void cancel(User actor, int appointmentId) {
        LocalDateTime old = repo.getStartAt(appointmentId);
        if (old == null) throw new IllegalArgumentException("Appointment not found");
        repo.updateStatus(appointmentId, AppointmentStatus.CANCELLED);
        hist.log(appointmentId, "CANCEL", old, null, actor.getId(), "cancelled");
    }

    public void reschedule(User actor, int appointmentId, int doctorId, LocalDateTime newStartAt) {
        val.notPast(newStartAt);
        if (repo.slotTaken(doctorId, newStartAt)) throw new IllegalArgumentException("New slot is taken");
        LocalDateTime old = repo.getStartAt(appointmentId);
        if (old == null) throw new IllegalArgumentException("Appointment not found");
        repo.reschedule(appointmentId, newStartAt);
        hist.log(appointmentId, "RESCHEDULE", old, newStartAt, actor.getId(), "moved");
    }

    public void markDone(int appointmentId, User actor) {
        LocalDateTime old = repo.getStartAt(appointmentId);
        if (old == null) throw new IllegalArgumentException("Appointment not found");
        repo.updateStatus(appointmentId, AppointmentStatus.DONE);
        hist.log(appointmentId, "COMPLETE", old, old, actor.getId(), "done");
    }

    public List<FullAppointmentDto> myAppointments(int patientId) {
        return repo.findByPatientFull(patientId);
    }

    public List<FullAppointmentDto> allAppointments() {
        return repo.findAllFull();
    }
}
