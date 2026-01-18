package com.company;

import com.company.controller.AppointmentController;
import com.company.entity.User;

import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        AppointmentController controller = new AppointmentController();
        User user = new User("Alice", "PATIENT");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    1. Book appointment
                    2. View appointments
                    3. Show doctors
                    4. Show users
                    5. Show free slots
                    6. Exit
                    """);

            int choice = sc.nextInt();

            if (choice == 1) {
                controller.showDoctors();
                controller.showUsers();

                System.out.print("Doctor ID: ");
                int doctorId = sc.nextInt();

                System.out.print("User ID: ");
                int userId = sc.nextInt();

                System.out.print("Date (YYYY-MM-DD): ");
                Date date = Date.valueOf(sc.next());

                controller.showFreeSlots(doctorId, date);

                System.out.print("Time (HH:MM): ");
                String time = sc.next();

                controller.bookAppointment(
                        user,
                        doctorId,
                        userId,
                        date,
                        Time.valueOf(time + ":00")
                );
            }

            if (choice == 2) controller.showAppointments();
            if (choice == 3) controller.showDoctors();
            if (choice == 4) controller.showUsers();

            if (choice == 5) {
                System.out.print("Doctor ID: ");
                int doctorId = sc.nextInt();
                System.out.print("Date (YYYY-MM-DD): ");
                Date date = Date.valueOf(sc.next());
                controller.showFreeSlots(doctorId, date);
            }

            if (choice == 6) break;
        }
    }
}
