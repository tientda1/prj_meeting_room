package dao.impl;

import dao.IEquipmentDAO;
import model.Equipment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAOImpl implements IEquipmentDAO {

    @Override
    public List<Equipment> getAllEquipments() {
        List<Equipment> equipments = new ArrayList<>();
        String sql = "SELECT * FROM equipments";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                equipments.add(new Equipment(
                        rs.getInt("id"),
                        rs.getString("equipment_name"),
                        rs.getInt("total_quantity"),
                        rs.getInt("available_quantity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipments;
    }

    @Override
    public boolean addEquipment(Equipment eq) {
        String sql = "INSERT INTO equipments (equipment_name, total_quantity, available_quantity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eq.getEquipmentName());
            ps.setInt(2, eq.getTotalQuantity());
            ps.setInt(3, eq.getAvailableQuantity());
            ps.setString(4, eq.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAvailableQuantity(int equipmentId, int availableQuantity) {
        String sql = "UPDATE equipments SET available_quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, availableQuantity);
            ps.setInt(2, equipmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEquipment(Equipment eq) {
        String sql = "UPDATE equipments SET equipment_name = ?, total_quantity = ?, available_quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eq.getEquipmentName());
            ps.setInt(2, eq.getTotalQuantity());
            ps.setInt(3, eq.getAvailableQuantity());
            ps.setInt(4, eq.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEquipment(int id) {
        String sql = "DELETE FROM equipments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("-> [LỖI] Ràng buộc dữ liệu: Không thể xóa thiết bị này vì nó đang nằm trong các đơn Đặt phòng!");
            return false;
        }
    }
}