package dao.impl;

import dao.IServiceDAO;
import model.Service;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImpl implements IServiceDAO {

    @Override
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("service_name"),
                        rs.getDouble("unit_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    @Override
    public boolean addService(Service service) {
        String sql = "INSERT INTO services (service_name, unit_price) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setDouble(2, service.getUnitPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateService(Service service) {
        String sql = "UPDATE services SET service_name = ?, unit_price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setDouble(2, service.getUnitPrice());
            ps.setInt(3, service.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteService(int id) {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("-> [LỖI] Xóa thất bại. Có thể dịch vụ này đang được tham chiếu trong các lịch đặt phòng (Booking).");
            return false;
        }
    }
}