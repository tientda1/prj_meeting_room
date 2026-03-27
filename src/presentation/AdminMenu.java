package presentation;

import model.Equipment;
import model.Room;
import service.AdminService;
import util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminService adminService = new AdminService();

    public static void display() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=== MENU QUẢN TRỊ VIÊN ===");
            System.out.println("1. Xem danh sách Phòng họp");
            System.out.println("2. Thêm Phòng họp mới");
            System.out.println("3. Sửa thông tin Phòng họp");
            System.out.println("4. Xóa Phòng họp");
            System.out.println("5. Xem danh sách Thiết bị");
            System.out.println("6. Thêm Thiết bị mới");
            System.out.println("7. Cập nhật số lượng thiết bị khả dụng");
            System.out.println("8. Tạo tài khoản Support Staff / Admin");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<Room> rooms = adminService.getAllRooms();
                    if (rooms.isEmpty()) {
                        System.out.println("-> Chưa có phòng họp nào.");
                    } else {
                        printRoomTable(rooms);
                    }
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
                    if(adminService.addNewRoom(name, cap, loc, fixEq)) {
                        System.out.println("-> Thêm phòng thành công!");
                    } else {
                        System.out.println("-> Thất bại. Tên phòng có thể đã tồn tại.");
                    }
                    break;
                case "3":
                    System.out.print("Nhập ID phòng cần sửa: ");
                    int editId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Tên phòng mới: ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Sức chứa mới: ");
                    int newCap = ValidationUtil.getValidInt(scanner);
                    System.out.print("Vị trí mới: ");
                    String newLoc = scanner.nextLine().trim();
                    System.out.print("Thiết bị cố định mới: ");
                    String newFixEq = scanner.nextLine().trim();
                    if(adminService.updateRoom(editId, newName, newCap, newLoc, newFixEq)) {
                        System.out.println("-> Cập nhật phòng thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại (Kiểm tra lại ID).");
                    }
                    break;
                case "4":
                    System.out.print("Nhập ID phòng cần xóa: ");
                    int delId = ValidationUtil.getValidInt(scanner);
                    if(adminService.deleteRoom(delId)) {
                        System.out.println("-> Xóa phòng thành công!");
                    } else {
                        System.out.println("-> Xóa thất bại. Có thể phòng này đang có lịch đặt (Booking).");
                    }
                    break;
                case "5":
                    List<Equipment> eqs = adminService.getAllEquipments();
                    if (eqs.isEmpty()) {
                        System.out.println("-> Chưa có thiết bị nào.");
                    } else {
                        printEquipmentTable(eqs);
                    }
                    break;
                case "6":
                    System.out.print("Tên thiết bị: ");
                    String eqName = scanner.nextLine().trim();
                    System.out.print("Tổng số lượng: ");
                    int total = ValidationUtil.getValidInt(scanner);
                    if(adminService.addNewEquipment(eqName, total)) {
                        System.out.println("-> Thêm thiết bị thành công!");
                    } else {
                        System.out.println("-> Thêm thiết bị thất bại.");
                    }
                    break;
                case "7":
                    System.out.print("Nhập ID thiết bị cần cập nhật: ");
                    int eqId = ValidationUtil.getValidInt(scanner);
                    System.out.print("Số lượng khả dụng hiện tại: ");
                    int available = ValidationUtil.getValidInt(scanner);
                    if(adminService.updateEquipmentAvailability(eqId, available)) {
                        System.out.println("-> Cập nhật số lượng thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại (Kiểm tra lại ID).");
                    }
                    break;
                case "8":
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

                    if(adminService.createSystemAccount(sUser, sPass, sRole, sName, sDept, sPhone, sEmail)) {
                        System.out.println("-> Tạo tài khoản " + sRole + " thành công!");
                    } else {
                        System.out.println("-> Tạo tài khoản thất bại.");
                    }
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
}