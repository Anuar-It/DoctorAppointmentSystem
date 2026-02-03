package com.company.controller;

import com.company.entity.Role;
import com.company.session.Session;
import com.company.util.ConsoleIO;

public class ConsoleController {
    private final ConsoleIO io = new ConsoleIO();
    private final Session session = new Session();

    private final AuthController auth = new AuthController(io, session);
    private final AppointmentController ap = new AppointmentController(io, session);
    private final DoctorController doctor = new DoctorController(io, session);
    private final AdminController admin = new AdminController(io, session);

    public void start() {
        while (true) {
            if (session.getCurrentUser() == null) startMenu();
            else roleMenu();
        }
    }

    private void startMenu() {
        io.println("1) Register");
        io.println("2) Login");
        io.println("3) Exit");
        int c = io.readInt("Choose: ");
        if (c == 1) auth.register();
        else if (c == 2) auth.login();
        else if (c == 3) System.exit(0);
        else io.println("Wrong option.");
    }

    private void roleMenu() {
        Role r = session.getCurrentUser().getRole();
        if (r == Role.PATIENT) patientMenu();
        else if (r == Role.DOCTOR) doctorMenu();
        else adminMenu();
    }

    private void patientMenu() {
        io.println("1) List doctors");
        io.println("2) Show free slots");
        io.println("3) Book appointment");
        io.println("4) My appointments");
        io.println("5) Cancel appointment");
        io.println("6) Reschedule appointment");
        io.println("7) Logout");
        int c = io.readInt("Choose: ");

        if (c == 1) ap.listDoctors();
        else if (c == 2) ap.showFreeSlots();
        else if (c == 3) ap.book();
        else if (c == 4) ap.myAppointments();
        else if (c == 5) ap.cancel();
        else if (c == 6) ap.reschedule();
        else if (c == 7) auth.logout();
        else io.println("Wrong option.");
    }

    private void doctorMenu() {
        io.println("1) Today schedule");
        io.println("2) Mark DONE");
        io.println("3) Logout");
        int c = io.readInt("Choose: ");

        if (c == 1) doctor.todaySchedule();
        else if (c == 2) doctor.markDone();
        else if (c == 3) auth.logout();
        else io.println("Wrong option.");
    }

    private void adminMenu() {
        io.println("1) Add specialization");
        io.println("2) Add doctor");
        io.println("3) Deactivate doctor");
        io.println("4) View all appointments");
        io.println("5) Logout");
        int c = io.readInt("Choose: ");

        if (c == 1) admin.addSpecialization();
        else if (c == 2) admin.addDoctor();
        else if (c == 3) admin.deactivateDoctor();
        else if (c == 4) admin.viewAllFull();
        else if (c == 5) auth.logout();
        else io.println("Wrong option.");
    }
}
