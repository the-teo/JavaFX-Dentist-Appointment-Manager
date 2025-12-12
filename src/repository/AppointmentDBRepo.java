package repository;

import domain.Appoitment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDBRepo extends AbstractDBRepo<Integer, Appoitment> {

    public AppointmentDBRepo(String URL) {
        super(URL);
    }

    @Override
    public Appoitment save(Appoitment entity) {
        String sql = "INSERT OR REPLACE INTO Appointments (id, patientId, appDate, appTime, status, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());
            ps.setInt(2, entity.getPatientId());
            ps.setString(3, entity.getDate());
            ps.setString(4, entity.getTime());
            ps.setString(5, entity.getStatus());
            ps.setString(6, entity.getDescription());
            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appoitment> findById(Integer id) {
        String sql = "SELECT * FROM Appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Appoitment> findAll() {
        List<Appoitment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                appointments.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading all appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM Appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(id) FROM Appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking appointment existence: " + e.getMessage(), e);
        }
    }

    private Appoitment mapResultSetToEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patientId");
        String date = rs.getString("appDate");
        String time = rs.getString("appTime");
        String status = rs.getString("status");
        String description = rs.getString("description");

        return new Appoitment(id, patientId, date, time, status, description);
    }
}