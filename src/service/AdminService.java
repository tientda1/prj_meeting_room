package service;

import dao.IEquipmentDAO;
import dao.IRoomDAO;
import dao.IServiceDAO;
import dao.IUserDAO;
import dao.impl.EquipmentDAOImpl;
import dao.impl.RoomDAOImpl;
import dao.impl.ServiceDAOImpl;
import dao.impl.UserDAOImpl;
import model.Equipment;
import model.Room;
import model.Service;
import model.User;
import util.PasswordHash;

import java.util.List;


public class AdminService {
    private final IRoomDAO roomDAO = new RoomDAOImpl();
    private final IEquipmentDAO equipmentDAO = new EquipmentDAOImpl();
    private final IUserDAO userDAO = new UserDAOImpl();
    private final IServiceDAO serviceDAO = new ServiceDAOImpl();

    // --- QUẢN LÝ PHÒNG HỌP ---
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public boolean addNewRoom(String name, int capacity, String location, String fixedEq) {
        Room room = new Room(0, name, capacity, location, fixedEq);
        return roomDAO.addRoom(room);
    }

    // BỔ SUNG: Sửa phòng
    public boolean updateRoom(int id, String name, int capacity, String location, String fixedEq) {
        Room room = new Room(id, name, capacity, location, fixedEq);
        return roomDAO.updateRoom(room);
    }

    // BỔ SUNG: Xóa phòng
    public boolean deleteRoom(int id) {
        return roomDAO.deleteRoom(id);
    }

    // --- QUẢN LÝ THIẾT BỊ ---
    public List<Equipment> getAllEquipments() {
        return equipmentDAO.getAllEquipments();
    }

    public boolean addNewEquipment(String name, int totalQuantity) {
        Equipment eq = new Equipment(0, name, totalQuantity, totalQuantity, "ACTIVE");
        return equipmentDAO.addEquipment(eq);
    }

    // BỔ SUNG: Cập nhật số lượng khả dụng
    public boolean updateEquipmentAvailability(int equipmentId, int availableQuantity) {
        return equipmentDAO.updateAvailableQuantity(equipmentId, availableQuantity);
    }

    // --- QUẢN LÝ NGƯỜI DÙNG ---
    // BỔ SUNG: Tạo tài khoản Support/Admin
    public boolean createSystemAccount(String username, String rawPassword, String role, String fullName, String dept, String phone, String email) {
        String hashedPass = PasswordHash.hashPassword(rawPassword);
        User user = new User(0, username, hashedPass, role, fullName, dept, phone, email);
        return userDAO.createStaffAccount(user);
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }

    public boolean addNewService(String name, double price) {
        Service service = new Service(0, name, price);
        return serviceDAO.addService(service);
    }

    public boolean updateService(int id, String name, double price) {
        Service service = new Service(id, name, price);
        return serviceDAO.updateService(service);
    }

    public boolean deleteService(int id) {
        return serviceDAO.deleteService(id);
    }
}