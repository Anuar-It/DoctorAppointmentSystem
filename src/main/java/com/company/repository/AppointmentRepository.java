package com.company.repository;

import com.company.db.DBConnection;
import com.company.dto.FullAppointmentDto;
import com.company.entity.AppointmentStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    public boolean slotTaken(int doctorId, LocalDateTime startAt) {
        String sql = "SELECT 1 FROM appointments WHERE doctor_id=? AND start_at=? AND status IN ('BOOKED','RESCHEDULED')";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setTimestamp(2, Timestamp.valueOf(startAt));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(int doctorId, int patientId, LocalDateTime startAt, String reason) {
        String sql = "INSERT INTO appointments(doctor_id, patient_id, start_at, status, reason) VALUES(?,?,?,?,?) RETURNING id";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.setTimestamp(3, Timestamp.valueOf(startAt));
            ps.setString(4, AppointmentStatus.BOOKED.name());
            ps.setString(5, reason);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(int appointmentId, AppointmentStatus status) {
        String sql = "UPDATE appointments SET status=?, updated_at=now() WHERE id=?";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime getStartAt(int appointmentId) {
        String sql = "SELECT start_at FROM appointments WHERE id=?";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getTimestamp("start_at").toLocalDateTime();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reschedule(int appointmentId, LocalDateTime newStartAt) {
        String sql = "UPDATE appointments SET start_at=?, status='RESCHEDULED', updated_at=now() WHERE id=?";
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(newStartAt));
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalTime> findBookedTimes(int doctorId, LocalDate day) {
        String sql = """
                SELECT start_at
                FROM appointments
                WHERE doctor_id=?
                  AND start_at >= ?
                  AND start_at < ?
                  AND status IN ('BOOKED','RESCHEDULED')
                ORDER BY start_at
                """;
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setTimestamp(2, Timestamp.valueOf(day.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(day.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                List<LocalTime> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(rs.getTimestamp("start_at").toLocalDateTime().toLocalTime());
                }
                return out;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<FullAppointmentDto> findByPatientFull(int patientId) {
        String sql = """
                SELECT a.id, a.start_at, a.status, a.reason,
                       pu.full_name AS patient_name,
                       du.full_name AS doctor_name,
                       s.name AS specialization,
                       d.cabinet
                FROM appointments a
                JOIN users pu ON pu.id = a.patient_id
                JOIN doctors d ON d.id = a.doctor_id
                JOIN users du ON du.id = d.user_id
                JOIN specializations s ON s.id = d.specialization_id
                WHERE a.patient_id = ?
                ORDER BY a.start_at DESC
                """;
        return queryFull(sql, patientId);
    }

    public List<FullAppointmentDto> findAllFull() {
        String sql = """
                SELECT a.id, a.start_at, a.status, a.reason,
                       pu.full_name AS patient_name,
                       du.full_name AS doctor_name,
                       s.name AS specialization,
                       d.cabinet
                FROM appointments a
                JOIN users pu ON pu.id = a.patient_id
                JOIN doctors d ON d.id = a.doctor_id
                JOIN users du ON du.id = d.user_id
                JOIN specializations s ON s.id = d.specialization_id
                ORDER BY a.start_at DESC
                """;
        return queryFull(sql);
    }

    public List<FullAppointmentDto> findDoctorDay(int doctorId, LocalDate day) {
        String sql = """
                SELECT a.id, a.start_at, a.status, a.reason,
                       pu.full_name AS patient_name,
                       du.full_name AS doctor_name,
                       s.name AS specialization,
                       d.cabinet
                FROM appointments a
                JOIN users pu ON pu.id = a.patient_id
                JOIN doctors d ON d.id = a.doctor_id
                JOIN users du ON du.id = d.user_id
                JOIN specializations s ON s.id = d.specialization_id
                WHERE a.doctor_id = ?
                  AND a.start_at >= ?
                  AND a.start_at < ?
                  AND a.status IN ('BOOKED','RESCHEDULED')
                ORDER BY a.start_at
                """;
        return queryFull(sql,
                doctorId,
                Timestamp.valueOf(day.atStartOfDay()),
                Timestamp.valueOf(day.plusDays(1).atStartOfDay()));
    }

    private List<FullAppointmentDto> queryFull(String sql, Object... params) {
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                Object p = params[i];
                if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else if (p instanceof Timestamp) ps.setTimestamp(i + 1, (Timestamp) p);
                else ps.setObject(i + 1, p);
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<FullAppointmentDto> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new FullAppointmentDto(
                            rs.getInt("id"),
                            rs.getTimestamp("start_at").toLocalDateTime(),
                            AppointmentStatus.fromDb(rs.getString("status")),
                            rs.getString("reason"),
                            rs.getString("patient_name"),
                            rs.getString("doctor_name"),
                            rs.getString("specialization"),
                            rs.getString("cabinet")
                    ));
                }
                return out;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
