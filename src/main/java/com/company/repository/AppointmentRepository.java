package com.company.repository;

import main.java.com.company.db.DBConnection;

import java.sql.*;
import java.util.HashSet;

public class AppointmentRepository {

    public boolean isSlotTaken(int doctorId, Date date, Time time) throws SQLException {
        String sql = "SELECT 1 FROM appointments WHERE doctor_id=? AND date=? AND time=? AND status='BOOKED'";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, doctorId);
        ps.setDate(2, date);
        ps.setTime(3, time);
        return ps.executeQuery().next();
    }

    public void save(int doctorId, int userId, Date date, Time time) throws SQLException {
        String sql = "INSERT INTO appointments (doctor_id, user_id, date, time, status) VALUES (?, ?, ?, ?, 'BOOKED')";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, doctorId);
        ps.setInt(2, userId);
        ps.setDate(3, date);
        ps.setTime(4, time);
        ps.executeUpdate();
    }

    public void getFullAppointments() throws SQLException {
        String sql = """
                SELECT a.id, d.name, u.name, a.date, a.time, a.status
                FROM appointments a
                JOIN doctors d ON a.doctor_id = d.id
                JOIN users u ON a.user_id = u.id
                """;

        Statement st = DBConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("ID | Doctor | Patient | Date | Time | Status");
        while (rs.next()) {
            System.out.println(
                    rs.getInt(1) + " | " +
                            rs.getString(2) + " | " +
                            rs.getString(3) + " | " +
                            rs.getDate(4) + " | " +
                            rs.getTime(5) + " | " +
                            rs.getString(6)
            );
        }
    }

    public void showDoctors() throws SQLException {
        String sql = "SELECT id, name, specialization FROM doctors ORDER BY id";
        Statement st = DBConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("ID | Name | Specialization");
        while (rs.next()) {
            System.out.println(
                    rs.getInt(1) + " | " +
                            rs.getString(2) + " | " +
                            rs.getString(3)
            );
        }
    }

    public void showUsers() throws SQLException {
        String sql = "SELECT id, name, role FROM users ORDER BY id";
        Statement st = DBConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("ID | Name | Role");
        while (rs.next()) {
            System.out.println(
                    rs.getInt(1) + " | " +
                            rs.getString(2) + " | " +
                            rs.getString(3)
            );
        }
    }

    public void showFreeSlots(int doctorId, Date date) throws SQLException {
        String[] slots = {
                "09:00","09:30","10:00","10:30","11:00","11:30",
                "12:00","12:30","13:00","13:30","14:00","14:30","15:00"
        };

        String sql = "SELECT time FROM appointments WHERE doctor_id=? AND date=? AND status='BOOKED'";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, doctorId);
        ps.setDate(2, date);
        ResultSet rs = ps.executeQuery();

        HashSet<String> booked = new HashSet<>();
        while (rs.next()) {
            booked.add(rs.getTime(1).toString().substring(0, 5));
        }

        System.out.println("Free slots:");
        boolean found = false;
        for (String s : slots) {
            if (!booked.contains(s)) {
                System.out.print(s + " ");
                found = true;
            }
        }
        System.out.println();
        if (!found) System.out.println("No free slots");
    }
}
