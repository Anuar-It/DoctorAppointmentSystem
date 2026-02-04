package com.company.controller;

import com.company.entity.Role;
import com.company.entity.User;
import com.company.service.AuthService;
import com.company.session.Session;
import com.company.util.ConsoleIO;

public class AuthController {
    private final ConsoleIO io;
    private final Session session;
    private final AuthService auth = new AuthService();

    public AuthController(ConsoleIO io, Session session) {
        this.io = io;
        this.session = session;
    }

    public void register() {
        try {
            String fullName = io.readLine("Full name: ");
            String username = io.readLine("Username: ");
            String password = io.readLine("Password: ");
            String roleRaw = io.readLine("Role (PATIENT/DOCTOR): ").toUpperCase();
            Role role = Role.valueOf(roleRaw);
            User u = auth.register(fullName, username, password, role);
            io.println("Registered id=" + u.getId());
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void login() {
        try {
            String username = io.readLine("Username: ");
            String password = io.readLine("Password: ");
            User u = auth.login(username, password);
            session.setCurrentUser(u);
            io.println("Welcome, " + u.getFullName() + " (" + u.getRole() + ")");
        } catch (Exception e) {
            io.println("Error: " + e.getMessage());
        }
    }

    public void logout() {
        session.clear();
        io.println("Logged out.");
    }
}
