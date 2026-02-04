package com.company.service;

import com.company.entity.Role;
import com.company.entity.User;
import com.company.repository.UserRepository;
import com.company.util.PasswordHasher;

import java.util.Optional;

public class AuthService {
    private final UserRepository users = new UserRepository();
    private final ValidationService val = new ValidationService();

    public User register(String fullName, String username, String password, Role role) {
        val.notBlank(fullName, "Full name");
        val.notBlank(username, "Username");
        val.notBlank(password, "Password");
        val.passwordRules(password);

        if (role == Role.ADMIN) throw new IllegalArgumentException("ADMIN cannot be registered here");

        if (users.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hash = PasswordHasher.sha256(password);
        return users.insert(fullName, username, hash, role);
    }

    public User login(String username, String password) {
        val.notBlank(username, "Username");
        val.notBlank(password, "Password");

        Optional<User> u = users.findByUsername(username);
        if (u.isEmpty()) throw new IllegalArgumentException("Wrong username/password");

        String hash = PasswordHasher.sha256(password);
        if (!hash.equals(u.get().getPasswordHash())) throw new IllegalArgumentException("Wrong username/password");

        return u.get();
    }
}
