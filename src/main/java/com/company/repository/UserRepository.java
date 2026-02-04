package com.company.repository;

import com.company.db.DBConnection;
import com.company.entity.Role;
import com.company.entity.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, full_name, username, password_hash, role, created_at FROM users WHERE username = ?";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User insert(String fullName, String username, String passwordHash, Role role) {
        String sql = "INSERT INTO users(full_name, username, password_hash, role) VALUES(?,?,?,?) RETURNING id, created_at";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setString(4, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int id = rs.getInt("id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                return new User(id, fullName, username, passwordHash, role, createdAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("password_hash"),
                Role.fromDb(rs.getString("role")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
