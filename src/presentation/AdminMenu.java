package presentation;

import dao.IBookingDAO;
import dao.IUserDAO;
import dao.impl.BookingDAOImpl;
import dao.impl.UserDAOImpl;
import model.Booking;
import model.Equipment;
import model.Room;
import model.User;
import service.AdminService;
import util.ValidationUtil;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminService adminService = new AdminService();
    private static final IBookingDAO bookingDAO = new BookingDAOImpl();
    private static final IUserDAO userDAO = new UserDAOImpl();

    public static void display() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Quản lý phòng họp");
            System.out.println("2. Quản lý thiết bị");
            System.out.println("3. Quản lý dịch vụ đi kèm");
            System.out.println("4. Quản lý người dùng");
            System.out.println("5. Duyệt & Phân công Đặt phòng");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    roomMenu();
                    break;
                case "2":
                    equipmentMenu();
                    break;
                case "3":
                    serviceMenu();
                    break;
                case "4":
                    userMenu();
                    break;
                case "5":
                    bookingMenu();
                    break;
                case "0":
                    System.out.println("-> Đã đăng xuất.");
                    Main.loggedInUser = null;
                    isRunning = false;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- 1. SUB-MENU QUẢN LÝ PHÒNG HỌP ---
    private static void roomMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- 1. QUẢN LÝ PHÒNG HỌP ---");
            System.out.println("1. Xem danh sách phòng họp");
            System.out.println("2. Thêm phòng họp mới");
            System.out.println("3. Sửa thông tin phòng họp");
            System.out.println("4. Xóa phòng họp");
            System.out.println("5. Tìm kiếm phòng họp theo tên");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<Room> rooms = adminService.getAllRooms();
                    if (rooms.isEmpty()) System.out.println("-> Chưa có phòng họp nào.");
                    else printRoomTable(rooms);
                    break;
                case "2":
                    System.out.print("Tên phòng: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Sức chứa: ");
                    int cap = ValidationUtil.getValidInt(scanner);
                    System.out.print("Vị trí: ");
                    String loc = scanner.nextLine().trim();
                    System.out.print("Thiết bị cố định: ");
                    String fixEq = scanner.nextLine().trim();
                    if (adminService.addNewRoom(name, cap, loc, fixEq)) {
                        System.out.println("-> Thêm phòng thành công!");
                    } else {
                        System.out.println("-> Thất bại. Tên phòng có thể đã tồn tại.");
                    }
                    break;
                case "3":
                    System.out.print("Nhập ID phòng cần sửa: ");
                    int editId = ValidationUtil.getValidInt(scanner);

                    // Lấy thông tin cũ hiển thị cho người dùng
                    Room oldRoom = adminService.getAllRooms().stream().filter(r -> r.getId() == editId).findFirst().orElse(null);
                    if (oldRoom == null) {
                        System.out.println("-> Không tìm thấy phòng với ID này!");
                        break;
                    }
                    System.out.println("--- Thông tin cũ ---");
                    System.out.printf("Tên: %s | Sức chứa: %d | Vị trí: %s | TB: %s%n",
                            oldRoom.getRoomName(), oldRoom.getCapacity(), oldRoom.getLocation(), oldRoom.getFixedEquipments());
                    System.out.println("--------------------");

                    System.out.print("Tên phòng mới: ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Sức chứa mới: ");
                    int newCap = ValidationUtil.getValidInt(scanner);
                    System.out.print("Vị trí mới: ");
                    String newLoc = scanner.nextLine().trim();
                    System.out.print("Thiết bị cố định mới: ");
                    String newFixEq = scanner.nextLine().trim();
                    if (adminService.updateRoom(editId, newName, newCap, newLoc, newFixEq)) {
                        System.out.println("-> Cập nhật phòng thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại.");
                    }
                    break;
                case "4":
                    System.out.print("Nhập ID phòng cần xóa: ");
                    int delId = ValidationUtil.getValidInt(scanner);
                    if (adminService.deleteRoom(delId)) {
                        System.out.println("-> Xóa phòng thành công!");
                    } else {
                        System.out.println("-> Xóa thất bại. Có thể phòng này đang có lịch đặt.");
                    }
                    break;
                case "5":
                    System.out.print("Nhập tên phòng cần tìm: ");
                    String keyword = scanner.nextLine().trim().toLowerCase();
                    List<Room> searchResults = adminService.getAllRooms().stream()
                            .filter(r -> r.getRoomName().toLowerCase().contains(keyword))
                            .collect(Collectors.toList());
                    if (searchResults.isEmpty()) System.out.println("-> Không tìm thấy phòng nào phù hợp.");
                    else printRoomTable(searchResults);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- 2. SUB-MENU QUẢN LÝ THIẾT BỊ ---
    private static void equipmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- 2. QUẢN LÝ THIẾT BỊ ---");
            System.out.println("1. Xem danh sách thiết bị");
            System.out.println("2. Thêm thiết bị mới");
            System.out.println("3. Cập nhật số lượng khả dụng");
            System.out.println("4. Sửa thông tin thiết bị");
            System.out.println("5. Xóa thiết bị");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<Equipment> eqs = adminService.getAllEquipments();
                    if (eqs.isEmpty()) System.out.println("-> Chưa có thiết bị nào.");
                    else printEquipmentTable(eqs);
                    break;
                case "2":
                    System.out.print("Tên thiết bị: ");
                    String eqName = scanner.nextLine().trim();
                    System.out.print("Tổng số lượng: ");
                    int total = ValidationUtil.getValidInt(scanner);
                    if (adminService.addNewEquipment(eqName, total)) System.out.println("-> Thêm thiết bị thành công!");
                    break;
                case "3":
                    System.out.print("Nhập ID thiết bị cần cập nhật: ");
                    int eqId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Số lượng khả dụng hiện tại: ");
                    int available = ValidationUtil.getValidInt(scanner);
                    if (adminService.updateEquipmentAvailability(eqId, available))
                        System.out.println("-> Cập nhật số lượng thành công!");
                    else System.out.println("-> Cập nhật thất bại.");
                    break;
                case "4":
                    System.out.print("Nhập ID thiết bị cần sửa: ");
                    int editEqId = ValidationUtil.getValidInt(scanner);

                    // Lấy thông tin cũ hiển thị
                    Equipment oldEq = adminService.getAllEquipments().stream().filter(e -> e.getId() == editEqId).findFirst().orElse(null);
                    if (oldEq == null) {
                        System.out.println("-> Không tìm thấy thiết bị với ID này!");
                        break;
                    }
                    System.out.println("--- Thông tin cũ ---");
                    System.out.printf("Tên: %s | Tổng cộng: %d | Khả dụng: %d%n",
                            oldEq.getEquipmentName(), oldEq.getTotalQuantity(), oldEq.getAvailableQuantity());
                    System.out.println("--------------------");

                    System.out.print("Tên thiết bị mới: ");
                    String newEqName = scanner.nextLine().trim();
                    System.out.print("Tổng số lượng mới: ");
                    int newTotal = ValidationUtil.getValidInt(scanner);
                    System.out.print("Số lượng khả dụng mới: ");
                    int newAvailable = ValidationUtil.getValidInt(scanner);

                    if (adminService.updateEquipmentFull(editEqId, newEqName, newTotal, newAvailable)) {
                        System.out.println("-> Cập nhật thiết bị thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại.");
                    }
                    break;
                case "5":
                    System.out.print("Nhập ID thiết bị cần xóa: ");
                    int delEqId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Bạn có chắc chắn muốn xóa thiết bị này? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        if (adminService.deleteEquipment(delEqId)) {
                            System.out.println("-> Xóa thiết bị thành công!");
                        }
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- 3. SUB-MENU DỊCH VỤ ---
    private static void serviceMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- 3. QUẢN LÝ DỊCH VỤ ĐI KÈM ---");
            System.out.println("1. Xem danh sách dịch vụ");
            System.out.println("2. Thêm dịch vụ mới");
            System.out.println("3. Sửa thông tin dịch vụ");
            System.out.println("4. Xóa dịch vụ");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<model.Service> services = adminService.getAllServices();
                    if (services.isEmpty()) System.out.println("-> Chưa có dịch vụ nào.");
                    else printServiceTable(services);
                    break;
                case "2":
                    System.out.print("Tên dịch vụ: ");
                    String sName = scanner.nextLine().trim();
                    System.out.print("Đơn giá (VD: 15000): ");
                    double sPrice = ValidationUtil.getValidDouble(scanner);
                    if (adminService.addNewService(sName, sPrice)) {
                        System.out.println("-> Thêm dịch vụ thành công!");
                    } else {
                        System.out.println("-> Thêm dịch vụ thất bại.");
                    }
                    break;
                case "3":
                    System.out.print("Nhập ID dịch vụ cần sửa: ");
                    int editId = ValidationUtil.getValidInt(scanner);

                    model.Service oldSvc = adminService.getAllServices().stream().filter(s -> s.getId() == editId).findFirst().orElse(null);
                    if (oldSvc == null) {
                        System.out.println("-> Không tìm thấy dịch vụ với ID này!");
                        break;
                    }
                    System.out.println("--- Thông tin cũ ---");
                    System.out.printf("Tên: %s | Đơn giá: %.2f%n", oldSvc.getServiceName(), oldSvc.getUnitPrice());
                    System.out.println("--------------------");

                    System.out.print("Tên dịch vụ mới: ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Đơn giá mới: ");
                    double newPrice = ValidationUtil.getValidDouble(scanner);

                    if (adminService.updateService(editId, newName, newPrice)) {
                        System.out.println("-> Cập nhật dịch vụ thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại.");
                    }
                    break;
                case "4":
                    System.out.print("Nhập ID dịch vụ cần xóa: ");
                    int delId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Bạn có chắc chắn muốn xóa dịch vụ này không? (Y/N): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                        if (adminService.deleteService(delId)) {
                            System.out.println("-> Xóa dịch vụ thành công!");
                        }
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- 4. SUB-MENU NGƯỜI DÙNG ---
    private static void userMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- 4. QUẢN LÝ NGƯỜI DÙNG ---");
            System.out.println("1. Xem danh sách Nhân viên Hỗ trợ (Support)");
            System.out.println("2. Tạo tài khoản Support Staff / Admin");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<User> supports = userDAO.getUsersByRole("SUPPORT");
                    if (supports.isEmpty()) System.out.println("-> Chưa có nhân viên hỗ trợ nào.");
                    else {
                        System.out.println("+----+----------------------+---------------------------+");
                        System.out.printf("| %-2s | %-20s | %-25s |%n", "ID", "Tên đăng nhập", "Họ và tên");
                        System.out.println("+----+----------------------+---------------------------+");
                        for (User u : supports) {
                            System.out.printf("| %-2d | %-20s | %-25s |%n", u.getId(), u.getUsername(), u.getFullName());
                        }
                        System.out.println("+----+----------------------+---------------------------+");
                    }
                    break;
                case "2":
                    System.out.println("--- TẠO TÀI KHOẢN NỘI BỘ ---");
                    System.out.print("Tên đăng nhập: ");
                    String sUser = scanner.nextLine().trim();
                    System.out.print("Mật khẩu: ");
                    String sPass = scanner.nextLine().trim();
                    System.out.print("Vai trò (SUPPORT / ADMIN): ");
                    String sRole = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Họ và tên: ");
                    String sName = scanner.nextLine().trim();
                    System.out.print("Phòng ban: ");
                    String sDept = scanner.nextLine().trim();
                    System.out.print("Số điện thoại nội bộ: ");
                    String sPhone = scanner.nextLine().trim();
                    System.out.print("Email: ");
                    String sEmail = scanner.nextLine().trim();

                    if (adminService.createSystemAccount(sUser, sPass, sRole, sName, sDept, sPhone, sEmail)) {
                        System.out.println("-> Tạo tài khoản " + sRole + " thành công!");
                    } else {
                        System.out.println("-> Tạo tài khoản thất bại.");
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- 5. SUB-MENU DUYỆT BOOKING ---
    private static void bookingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- 5. DUYỆT & PHÂN CÔNG ĐẶT PHÒNG ---");
            System.out.println("1. Xem các yêu cầu chờ duyệt (PENDING)");
            System.out.println("2. Xử lý yêu cầu (Duyệt/Từ chối)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<Booking> pendingBookings = bookingDAO.getBookingsByStatus("PENDING");
                    if (pendingBookings.isEmpty()) {
                        System.out.println("-> Không có yêu cầu nào đang chờ duyệt.");
                    } else {
                        System.out.println("\n--- CÁC YÊU CẦU ĐANG CHỜ DUYỆT (PENDING) ---");
                        printBookingTable(pendingBookings);
                    }
                    break;
                case "2":
                    System.out.print("Nhập ID Booking cần xử lý: ");
                    int bId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Bạn muốn (1) Duyệt hay (2) Từ chối? : ");
                    int action = ValidationUtil.getValidInt(scanner);

                    if (action == 2) {
                        bookingDAO.updateBookingStatus(bId, "REJECTED");
                        System.out.println("-> Đã từ chối Booking.");
                    } else if (action == 1) {
                        List<User> staffs = userDAO.getUsersByRole("SUPPORT");
                        System.out.println("--- DANH SÁCH NHÂN VIÊN HỖ TRỢ ---");
                        staffs.forEach(s -> System.out.println("ID: " + s.getId() + " - Tên: " + s.getFullName()));
                        System.out.print("Nhập ID Support Staff để phân công: ");
                        int staffId = ValidationUtil.getValidInt(scanner);
                        if (bookingDAO.assignSupportStaff(bId, staffId)) {
                            System.out.println("-> Đã duyệt và phân công thành công!");
                        }
                    } else {
                        System.out.println("-> Lựa chọn không hợp lệ.");
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void printRoomTable(List<Room> rooms) {
        System.out.println("+----+----------------------+------------+-----------------+---------------------------+");
        System.out.printf("| %-2s | %-20s | %-10s | %-15s | %-25s |%n", "ID", "Tên phòng", "Sức chứa", "Vị trí", "Thiết bị cố định");
        System.out.println("+----+----------------------+------------+-----------------+---------------------------+");
        for (Room r : rooms) {
            String fixedEq = r.getFixedEquipments() != null ? r.getFixedEquipments() : "Không có";
            System.out.printf("| %-2d | %-20s | %-10d | %-15s | %-25s |%n",
                    r.getId(), r.getRoomName(), r.getCapacity(), r.getLocation(), fixedEq);
        }
        System.out.println("+----+----------------------+------------+-----------------+---------------------------+");
    }

    private static void printEquipmentTable(List<Equipment> eqs) {
        System.out.println("+----+----------------------+------------+------------+------------+");
        System.out.printf("| %-2s | %-20s | %-10s | %-10s | %-10s |%n", "ID", "Tên thiết bị", "Tổng cộng", "Khả dụng", "Trạng thái");
        System.out.println("+----+----------------------+------------+------------+------------+");
        for (Equipment e : eqs) {
            String status = e.getStatus() != null ? e.getStatus() : "N/A";
            System.out.printf("| %-2d | %-20s | %-10d | %-10d | %-10s |%n",
                    e.getId(), e.getEquipmentName(), e.getTotalQuantity(), e.getAvailableQuantity(), status);
        }
        System.out.println("+----+----------------------+------------+------------+------------+");
    }

    private static void printBookingTable(List<Booking> bookings) {
        System.out.println("+----+---------+---------+------------------+------------------+------------+");
        System.out.printf("| %-2s | %-7s | %-7s | %-16s | %-16s | %-10s |%n", "ID", "User ID", "Room ID", "Bắt đầu", "Kết thúc", "Trạng thái");
        System.out.println("+----+---------+---------+------------------+------------------+------------+");
        for (Booking b : bookings) {
            String start = util.DateUtil.format(b.getStartTime());
            String end = util.DateUtil.format(b.getEndTime());
            System.out.printf("| %-2d | %-7d | %-7d | %-16s | %-16s | %-10s |%n",
                    b.getId(), b.getUserId(), b.getRoomId(), start, end, b.getStatus());
        }
        System.out.println("+----+---------+---------+------------------+------------------+------------+");
    }

    private static void printServiceTable(List<model.Service> services) {
        System.out.println("+----+--------------------------------+-----------------+");
        System.out.printf("| %-2s | %-30s | %-15s |%n", "ID", "Tên dịch vụ", "Đơn giá (VNĐ)");
        System.out.println("+----+--------------------------------+-----------------+");
        for (model.Service s : services) {
            System.out.printf("| %-2d | %-30s | %-15.2f |%n", s.getId(), s.getServiceName(), s.getUnitPrice());
        }
        System.out.println("+----+--------------------------------+-----------------+");
    }
}