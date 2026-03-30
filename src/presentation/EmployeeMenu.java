package presentation;

import model.Booking;
import model.Equipment;
import model.Room;
import model.User;
import service.AdminService;
import service.BookingService;
import service.UserService;
import util.DateUtil;
import util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeeMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminService adminService = new AdminService(); // Mượn tạm để lấy danh sách phòng/thiết bị
    private static final BookingService bookingService = new BookingService();
    private static final UserService userService = new UserService();

    public static void display() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=== MENU NHÂN VIÊN (EMPLOYEE) ===");
            System.out.println("1. Xem danh sách Phòng họp");
            System.out.println("2. Đặt phòng (Booking)");
            System.out.println("3. Xem lịch sử đặt phòng của tôi");
            System.out.println("4. Xem & Cập nhật hồ sơ cá nhân");
            System.out.println("0. Đăng xuất");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayRooms();
                    break;
                case "2":
                    handleBooking();
                    break;
                case "3":
                    List<Booking> myBookings = bookingService.getMyBookings(Main.loggedInUser.getId());
                    if (myBookings.isEmpty()) {
                        System.out.println("-> Bạn chưa có lịch sử đặt phòng nào.");
                    } else {
                        System.out.println("\n--- LỊCH SỬ ĐẶT PHÒNG CỦA TÔI ---");
                        myBookings.forEach(b -> System.out.printf("ID: %d | Phòng: %d | Bắt đầu: %s | Trạng thái: %s%n",
                                b.getId(), b.getRoomId(), DateUtil.format(b.getStartTime()), b.getStatus()));
                    }
                    break;
                case "4":
                    handleProfile();
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

    private static void displayRooms() {
        List<Room> rooms = adminService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("-> Hiện tại chưa có phòng họp nào trong hệ thống.");
            return;
        }
        System.out.println("\n--- DANH SÁCH PHÒNG HỌP ---");
        System.out.println("+----+----------------------+------------+-----------------+");
        System.out.printf("| %-2s | %-20s | %-10s | %-15s |%n", "ID", "Tên phòng", "Sức chứa", "Vị trí");
        System.out.println("+----+----------------------+------------+-----------------+");
        for (Room r : rooms) {
            System.out.printf("| %-2d | %-20s | %-10d | %-15s |%n", r.getId(), r.getRoomName(), r.getCapacity(), r.getLocation());
        }
        System.out.println("+----+----------------------+------------+-----------------+");
    }

    private static void handleBooking() {
        System.out.println("\n--- TẠO ĐƠN ĐẶT PHÒNG ---");
        displayRooms();
        System.out.print("Nhập ID phòng muốn đặt (Hoặc nhập 0 để hủy): ");
        int roomId = ValidationUtil.getValidInt(scanner);
        if (roomId == 0) return;

        // Nhập ngày giờ
        System.out.println("\n[ĐỊNH DẠNG THỜI GIAN: yyyy-MM-dd HH:mm]");
        LocalDateTime startTime = ValidationUtil.getValidDateTime(scanner, "Thời gian BẮT ĐẦU (VD: 2026-04-10 08:00): ");
        LocalDateTime endTime = ValidationUtil.getValidDateTime(scanner, "Thời gian KẾT THÚC (VD: 2026-04-10 11:30): ");

        // Chọn mượn thiết bị
        Map<Integer, Integer> borrowedEquipments = new HashMap<>();
        System.out.print("\nBạn có muốn mượn thêm thiết bị di động không? (Y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            List<Equipment> eqs = adminService.getAllEquipments();
            if (eqs.isEmpty()) {
                System.out.println("-> Không có thiết bị nào khả dụng.");
            } else {
                boolean addingEq = true;
                while (addingEq) {
                    System.out.println("\n--- DANH SÁCH THIẾT BỊ ---");
                    for (Equipment e : eqs) {
                        System.out.printf("ID: %d | Tên: %s | Khả dụng: %d%n", e.getId(), e.getEquipmentName(), e.getAvailableQuantity());
                    }
                    System.out.print("Nhập ID thiết bị muốn mượn (Nhập 0 để dừng chọn): ");
                    int eqId = ValidationUtil.getValidInt(scanner);
                    if (eqId == 0) {
                        addingEq = false;
                    } else {
                        System.out.print("Nhập số lượng mượn: ");
                        int qty = ValidationUtil.getValidInt(scanner);
                        if (qty > 0) {
                            borrowedEquipments.put(eqId, qty);
                            System.out.println("-> Đã thêm vào danh sách mượn.");
                        }
                    }
                }
            }
        }

        System.out.println("\nĐang xử lý yêu cầu...");
        int currentUserId = Main.loggedInUser.getId();
        boolean isSuccess = bookingService.createBooking(currentUserId, roomId, startTime, endTime, borrowedEquipments);

        if (isSuccess) {
            System.out.println("-> [THÀNH CÔNG] Đặt phòng hoàn tất. Trạng thái hiện tại: PENDING (Chờ Admin duyệt).");
        }
    }
    private static void handleProfile() {
        User currentUser = Main.loggedInUser;
        System.out.println("\n--- HỒ SƠ CÁ NHÂN ---");
        System.out.println("1. Họ và tên: " + currentUser.getFullName());
        System.out.println("2. Phòng ban: " + currentUser.getDepartment());
        System.out.println("3. Số điện thoại nội bộ: " + currentUser.getPhoneExt());
        System.out.println("4. Email: " + currentUser.getEmail());

        System.out.print("\nBạn có muốn cập nhật thông tin không? (Y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("[Hướng dẫn: Nhập thông tin mới. Nếu muốn giữ nguyên thông tin cũ, hãy để trống và nhấn Enter]");

            System.out.print("Họ và tên mới [" + currentUser.getFullName() + "]: ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) currentUser.setFullName(newName);

            System.out.print("Phòng ban mới [" + currentUser.getDepartment() + "]: ");
            String newDept = scanner.nextLine().trim();
            if (!newDept.isEmpty()) currentUser.setDepartment(newDept);

            System.out.print("SĐT nội bộ mới [" + currentUser.getPhoneExt() + "]: ");
            String newPhone = scanner.nextLine().trim();
            if (!newPhone.isEmpty()) currentUser.setPhoneExt(newPhone);

            System.out.print("Email mới [" + currentUser.getEmail() + "]: ");
            String newEmail = scanner.nextLine().trim();
            if (!newEmail.isEmpty()) currentUser.setEmail(newEmail);

            if (userService.updateProfile(currentUser)) {
                System.out.println("-> Cập nhật hồ sơ thành công!");
                Main.loggedInUser = currentUser;
            } else {
                System.out.println("-> Cập nhật thất bại.");
            }
        }
    }
}