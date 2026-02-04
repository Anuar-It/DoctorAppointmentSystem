package com.company.repository;

import com.company.db.DBConnection;
import com.company.entity.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {

    public List<Doctor> findAllActive() {
        String sql = """
                SELECT d.id, d.user_id, u.full_name AS doctor_name, s.name AS specialization, d.cabinet, d.active
                FROM doctors d
                JOIN users u ON u.id = d.user_id
                JOIN specializations s ON s.id = d.specialization_id
                WHERE d.active = true
                ORDER BY doctor_name
                """;
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Doctor> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Doctor(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("doctor_name"),
                        rs.getString("specialization"),
                        rs.getString("cabinet"),
                        rs.getBoolean("active")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer findDoctorIdByUserId(int userId) {
        String sql = "SELECT id FROM doctors WHERE user_id=? AND active=true";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertDoctor(int userId, int specializationId, String cabinet) {
        String sql = "INSERT INTO doctors(user_id, specialization_id, cabinet, active) VALUES(?,?,?,true)";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, specializationId);
            ps.setString(3, cabinet);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivate(int doctorId) {
        String sql = "UPDATE doctors SET active=false WHERE id=?";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
