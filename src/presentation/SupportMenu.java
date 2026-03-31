package presentation;

import model.Booking;
import service.SupportService;
import util.DateUtil;
import util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

public class SupportMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SupportService supportService = new SupportService();

    public static void display() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=== MENU NHÂN VIÊN HỖ TRỢ (SUPPORT) ===");
            System.out.println("1. Xem danh sách công việc được phân công");
            System.out.println("2. Cập nhật trạng thái phòng họp");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    List<Booking> tasks = supportService.getAssignedTasks(Main.loggedInUser.getId());
                    if (tasks.isEmpty()) {
                        System.out.println("-> Hiện tại chưa có công việc nào được phân công.");
                    } else {
                        System.out.println("\n--- DANH SÁCH CÔNG VIỆC ---");
                        System.out.println("+----+---------+------------------+------------------+------------------+");
                        System.out.printf("| %-2s | %-7s | %-16s | %-16s | %-16s |%n", "ID", "Phòng", "Bắt đầu", "Kết thúc", "Trạng thái");
                        System.out.println("+----+---------+------------------+------------------+------------------+");
                        for (Booking b : tasks) {
                            System.out.printf("| %-2d | %-7d | %-16s | %-16s | %-16s |%n",
                                    b.getId(), b.getRoomId(), DateUtil.format(b.getStartTime()), DateUtil.format(b.getEndTime()), b.getStatus());
                        }
                        System.out.println("+----+---------+------------------+------------------+------------------+");
                    }
                    break;
                case "2":
                    System.out.print("Nhập ID Đặt phòng (Booking ID) cần cập nhật: ");
                    int bookingId = ValidationUtil.getValidInt(scanner);
                    System.out.println("Chọn trạng thái mới:");
                    System.out.println("1. PREPARING (Đang chuẩn bị)");
                    System.out.println("2. READY (Đã sẵn sàng)");
                    System.out.println("3. LACK_EQUIPMENT (Thiếu thiết bị)");
                    System.out.print("Lựa chọn: ");
                    int statusChoice = ValidationUtil.getValidInt(scanner);
                    String newStatus = "";
                    if (statusChoice == 1) newStatus = "PREPARING";
                    else if (statusChoice == 2) newStatus = "READY";
                    else if (statusChoice == 3) newStatus = "LACK_EQUIPMENT";
                    else {
                        System.out.println("-> Lựa chọn không hợp lệ.");
                        break;
                    }

                    if (supportService.updateTaskStatus(bookingId, newStatus)) {
                        System.out.println("-> Cập nhật trạng thái thành công!");
                    } else {
                        System.out.println("-> Cập nhật thất bại.");
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
}