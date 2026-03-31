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
import java.util.stream.Collectors;

public class EmployeeMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AdminService adminService = new AdminService();
    private static final BookingService bookingService = new BookingService();
    private static final UserService userService = new UserService();

    public static void display() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=== MENU NHÂN VIÊN (EMPLOYEE) ===");
            System.out.println("1. Xem danh sách tất cả Phòng họp");
            System.out.println("2. Đặt phòng & Yêu cầu dịch vụ (Tìm theo giờ trống)");
            System.out.println("3. Xem lịch sử đặt phòng của tôi");
            System.out.println("4. Hủy Đơn đặt phòng (PENDING)");
            System.out.println("5. Xem & Cập nhật hồ sơ cá nhân");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": displayRooms(adminService.getAllRooms()); break;
                case "2": handleBooking(); break;
                case "3": showMyBookings(); break;
                case "4": handleCancelBooking(); break;
                case "5": handleProfile(); break;
                case "0":
                    System.out.println("-> Đã đăng xuất.");
                    Main.loggedInUser = null;
                    isRunning = false;
                    break;
                default: System.out.println("-> Lựa chọn không hợp lệ.");
            }
        }
    }

    // --- LUỒNG ĐẶT PHÒNG CHUẨN ---
    private static void handleBooking() {
        System.out.println("\n--- TẠO ĐƠN ĐẶT PHÒNG MỚI ---");

        // 1. Nhập và kiểm tra thời gian
        System.out.println("[ĐỊNH DẠNG THỜI GIAN: yyyy-MM-dd HH:mm]");
        LocalDateTime startTime = ValidationUtil.getValidDateTime(scanner, "Thời gian BẮT ĐẦU (VD: 2026-04-15 08:00): ");
        if (startTime.isBefore(LocalDateTime.now())) {
            System.out.println("-> [TỪ CHỐI] Không thể đặt phòng trong quá khứ.");
            return;
        }

        LocalDateTime endTime = ValidationUtil.getValidDateTime(scanner, "Thời gian KẾT THÚC (VD: 2026-04-15 11:30): ");
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            System.out.println("-> [TỪ CHỐI] Thời gian kết thúc phải diễn ra sau thời gian bắt đầu.");
            return;
        }

        // 2. Nhập số người tham gia dự kiến
        System.out.print("Số người tham gia dự kiến: ");
        int expectedAttendees = ValidationUtil.getValidInt(scanner);

        // 3. Lọc danh sách phòng TRỐNG và ĐỦ SỨC CHỨA
        System.out.println("\n-> Đang tìm kiếm phòng phù hợp...");
        List<Room> allRooms = adminService.getAllRooms();
        List<Room> availableRooms = allRooms.stream()
                .filter(r -> r.getCapacity() >= expectedAttendees)
                .filter(r -> bookingService.isRoomAvailable(r.getId(), startTime, endTime))
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            System.out.println("-> [RẤT TIẾC] Không có phòng nào trống và đủ sức chứa trong khung giờ này. Vui lòng chọn giờ khác!");
            return;
        }

        System.out.println("--- DANH SÁCH PHÒNG TRỐNG PHÙ HỢP ---");
        displayRooms(availableRooms);

        // 4. Chọn phòng
        System.out.print("Nhập ID phòng muốn đặt (Hoặc nhập 0 để hủy thao tác): ");
        int roomId = ValidationUtil.getValidInt(scanner);
        if (roomId == 0) return;

        // Kiểm tra xem ID nhập vào có nằm trong danh sách phòng trống không
        boolean validRoom = availableRooms.stream().anyMatch(r -> r.getId() == roomId);
        if (!validRoom) {
            System.out.println("-> [LỖI] ID phòng không hợp lệ hoặc không có trong danh sách phòng trống!");
            return;
        }

        // 5. Chọn Thiết bị mượn thêm
        Map<Integer, Integer> borrowedEquipments = new HashMap<>();
        System.out.print("\nBạn có muốn mượn thêm thiết bị di động không? (Y/N): ");
        if (ValidationUtil.getNonEmptyString(scanner, "").equalsIgnoreCase("Y")) {
            List<Equipment> eqs = adminService.getAllEquipments();
            while (true) {
                System.out.println("\n--- DANH SÁCH THIẾT BỊ ---");
                for (Equipment e : eqs) System.out.printf("ID: %d | Tên: %s | Khả dụng: %d%n", e.getId(), e.getEquipmentName(), e.getAvailableQuantity());

                System.out.print("Nhập ID thiết bị muốn mượn (Nhập 0 để XONG): ");
                int eqId = ValidationUtil.getValidInt(scanner);
                if (eqId == 0) break;

                System.out.print("Số lượng mượn: ");
                int qty = ValidationUtil.getValidInt(scanner);
                if (qty > 0) borrowedEquipments.put(eqId, qty);
            }
        }

        // 6. Chọn Dịch vụ đi kèm
        Map<Integer, Integer> orderedServices = new HashMap<>();
        System.out.print("\nBạn có muốn đặt thêm dịch vụ (Nước uống, bánh ngọt...) không? (Y/N): ");
        if (ValidationUtil.getNonEmptyString(scanner, "").equalsIgnoreCase("Y")) {
            List<model.Service> svcs = adminService.getAllServices();
            while (true) {
                System.out.println("\n--- DANH SÁCH DỊCH VỤ ---");
                for (model.Service s : svcs) System.out.printf("ID: %d | Tên: %s | Đơn giá: %.2f VNĐ%n", s.getId(), s.getServiceName(), s.getUnitPrice());

                System.out.print("Nhập ID dịch vụ muốn đặt (Nhập 0 để XONG): ");
                int svcId = ValidationUtil.getValidInt(scanner);
                if (svcId == 0) break;

                System.out.print("Số lượng: ");
                int qty = ValidationUtil.getValidInt(scanner);
                if (qty > 0) orderedServices.put(svcId, qty);
            }
        }

        // 7. Lưu xuống DB
        System.out.println("\nĐang xử lý yêu cầu...");
        if (bookingService.createBooking(Main.loggedInUser.getId(), roomId, startTime, endTime, borrowedEquipments, orderedServices)) {
            System.out.println("-> [THÀNH CÔNG] Đặt phòng hoàn tất. Trạng thái hiện tại: PENDING (Chờ Admin duyệt).");
        } else {
            System.out.println("-> [LỖI] Hệ thống gặp sự cố khi lưu đơn đặt phòng.");
        }
    }

    // --- XEM LỊCH SỬ ĐẶT PHÒNG ---
    private static void showMyBookings() {
        List<Booking> myBookings = bookingService.getMyBookings(Main.loggedInUser.getId());
        if (myBookings.isEmpty()) {
            System.out.println("-> Bạn chưa có lịch sử đặt phòng nào.");
        } else {
            System.out.println("\n--- LỊCH SỬ ĐẶT PHÒNG CỦA TÔI ---");
            System.out.println("+----+---------+------------------+------------------+------------------+");
            System.out.printf("| %-2s | %-7s | %-16s | %-16s | %-16s |%n", "ID", "Phòng", "Bắt đầu", "Kết thúc", "Trạng thái");
            System.out.println("+----+---------+------------------+------------------+------------------+");
            for (Booking b : myBookings) {
                System.out.printf("| %-2d | %-7d | %-16s | %-16s | %-16s |%n",
                        b.getId(), b.getRoomId(), DateUtil.format(b.getStartTime()), DateUtil.format(b.getEndTime()), b.getStatus());
            }
            System.out.println("+----+---------+------------------+------------------+------------------+");
        }
    }

    // --- HỦY ĐẶT PHÒNG ---
    private static void handleCancelBooking() {
        showMyBookings();
        System.out.print("\nNhập ID Đơn đặt phòng bạn muốn HỦY (Nhập 0 để thoát): ");
        int bId = ValidationUtil.getValidInt(scanner);
        if (bId == 0) return;

        System.out.print("Bạn có chắc chắn muốn hủy đơn này không? (Y/N): ");
        if (ValidationUtil.getNonEmptyString(scanner, "").equalsIgnoreCase("Y")) {
            if (bookingService.cancelBooking(bId, Main.loggedInUser.getId())) {
                System.out.println("-> [THÀNH CÔNG] Đã hủy đơn đặt phòng.");
            } else {
                System.out.println("-> [LỖI] Hủy thất bại do từ chối từ Cơ sở dữ liệu.");
            }
        } else {
            System.out.println("-> Đã hủy thao tác.");
        }
    }

    // --- KẺ BẢNG DANH SÁCH PHÒNG ---
    private static void displayRooms(List<Room> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("-> Không có phòng họp nào để hiển thị.");
            return;
        }
        System.out.println("+----+----------------------+------------+-----------------+");
        System.out.printf("| %-2s | %-20s | %-10s | %-15s |%n", "ID", "Tên phòng", "Sức chứa", "Vị trí");
        System.out.println("+----+----------------------+------------+-----------------+");
        for (Room r : rooms) {
            System.out.printf("| %-2d | %-20s | %-10d | %-15s |%n", r.getId(), r.getRoomName(), r.getCapacity(), r.getLocation());
        }
        System.out.println("+----+----------------------+------------+-----------------+");
    }

    // --- CẬP NHẬT HỒ SƠ ---
    private static void handleProfile() {
        User currentUser = Main.loggedInUser;
        System.out.println("\n--- HỒ SƠ CÁ NHÂN ---");
        System.out.println("1. Họ và tên: " + currentUser.getFullName());
        System.out.println("2. Phòng ban: " + currentUser.getDepartment());
        System.out.println("3. Số điện thoại nội bộ: " + currentUser.getPhoneExt());
        System.out.println("4. Email: " + currentUser.getEmail());

        System.out.print("\nBạn có muốn cập nhật thông tin không? (Y/N): ");
        if (ValidationUtil.getNonEmptyString(scanner, "").equalsIgnoreCase("Y")) {
            System.out.println("[Hướng dẫn: Nhập thông tin mới. Nếu muốn giữ nguyên thông tin cũ, hãy để trống]");

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