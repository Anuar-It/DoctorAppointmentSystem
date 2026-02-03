package com.company.repository;

import com.company.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentHistoryRepository {
    public void log(int appointmentId, String action, LocalDateTime oldStart, LocalDateTime newStart, int actorUserId, String note) {
        String sql = """
            INSERT INTO appointment_history(appointment_id, action, old_start_at, new_start_at, actor_user_id, note)
            VALUES(?,?,?,?,?,?)
        """;
        try (Connection c = DBConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.setString(2, action);
            ps.setTimestamp(3, oldStart == null ? null : Timestamp.valueOf(oldStart));
            ps.setTimestamp(4, newStart == null ? null : Timestamp.valueOf(newStart));
            ps.setInt(5, actorUserId);
            ps.setString(6, note);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
