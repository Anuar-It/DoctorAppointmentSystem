package com.company.controller;

import com.company.dto.FullAppointmentDto;
import com.company.entity.User;
import com.company.repository.AppointmentRepository;
import com.company.repository.DoctorRepository;
import com.company.service.AppointmentService;
import com.company.session.Session;
import com.company.util.ConsoleIO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentController {
    private final ConsoleIO io;
    private final Session session;

    private final DoctorRepository doctors = new DoctorRepository();
    private final AppointmentRepository apRepo = new AppointmentRepository();
    private final AppointmentService service = new AppointmentService();

    public AppointmentController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void listDoctors() {
        var list = doctors.findAllActive();
        if (list.isEmpty()) {
            io.println("No active doctors.");
            return;
        }
        for (var d : list) {
            io.println(d.getId() + ") " + d.getDoctorName() + " | " + d.getSpecialization() + " | cabinet: " + d.getCabinet());
        }
    }

    public void showFreeSlots() {
        try {
            int doctorId = io.readInt("Doctor id: ");
            LocalDate day = LocalDate.parse(io.readLine("Date (YYYY-MM-DD): "));

            List<LocalTime> all = generateSlots(LocalTime.of(9, 0), LocalTime.of(15, 0), Duration.ofMinutes(30));
            List<LocalTime> booked = apRepo.findBookedTimes(doctorId, day);

            List<LocalTime> free = all.stream()
                    .filter(t -> !booked.contains(t))
                    .collect(Collectors.toList());

            if (free.isEmpty()) {
                io.println("No free slots.");
            } else {
                for (LocalTime t : free) io.println(t.toString());
            }
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void book() {
        try {
            User u = session.getCurrentUser();
            int doctorId = io.readInt("Doctor id: ");
            String date = io.readLine("Date (YYYY-MM-DD): ");
            String time = io.readLine("Time (HH:MM): ");
            String reason = io.readLine("Reason: ");
            LocalDateTime startAt = LocalDateTime.parse(date + "T" + time + ":00");
            int id = service.book(u, doctorId, startAt, reason);
            io.println("Booked appointment id=" + id);
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void myAppointments() {
        User u = session.getCurrentUser();
        var list = service.myAppointments(u.getId());
        if (list.isEmpty()) {
            io.println("No appointments.");
            return;
        }
        for (FullAppointmentDto a : list) {
            io.println(a.appointmentId + " | " + a.startAt + " | " + a.status + " | " + a.doctorName + " | " + a.specialization);
        }
    }

    public void cancel() {
        try {
            int id = io.readInt("Appointment id: ");
            service.cancel(session.getCurrentUser(), id);
            io.println("Cancelled.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void reschedule() {
        try {
            int appointmentId = io.readInt("Appointment id: ");
            int doctorId = io.readInt("Doctor id: ");
            String date = io.readLine("New date (YYYY-MM-DD): ");
            String time = io.readLine("New time (HH:MM): ");
            LocalDateTime newStartAt = LocalDateTime.parse(date + "T" + time + ":00");
            service.reschedule(session.getCurrentUser(), appointmentId, doctorId, newStartAt);
            io.println("Rescheduled.");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    private List<LocalTime> generateSlots(LocalTime start, LocalTime endExclusive, Duration step) {
        ArrayList<LocalTime> slots = new ArrayList<>();
        LocalTime t = start;
        while (t.isBefore(endExclusive)) {
            slots.add(t);
            t = t.plus(step);
        }
        return slots;
    }
}
