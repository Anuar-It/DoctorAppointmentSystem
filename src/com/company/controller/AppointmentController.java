package com.company.controller;

import com.company.entity.User;
import com.company.repository.AppointmentRepository;

import java.sql.Date;
import java.sql.Time;

public class AppointmentController {

    private AppointmentRepository repository = new AppointmentRepository();

    public void bookAppointment(User user, int doctorId, int userId, Date date, Time time) throws Exception {
        if (!user.getRole().equals("PATIENT")) {
            System.out.println("Access denied");
            return;
        }

        if (repository.isSlotTaken(doctorId, date, time)) {
            System.out.println("This time slot is already booked");
            return;
        }

        repository.save(doctorId, userId, date, time);
        System.out.println("Appointment booked successfully");
    }

    public void showAppointments() throws Exception {
        repository.getFullAppointments();
    }

    public void showDoctors() throws Exception {
        repository.showDoctors();
    }

    public void showUsers() throws Exception {
        repository.showUsers();
    }

    public void showFreeSlots(int doctorId, Date date) throws Exception {
        repository.showFreeSlots(doctorId, date);
    }
}
