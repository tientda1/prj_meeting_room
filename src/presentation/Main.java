package presentation;

import model.User;
import service.UserService;
import util.ValidationUtil;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    public static User loggedInUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== HỆ THỐNG QUẢN LÝ ĐẶT PHÒNG HỌP ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký tài khoản (Employee)");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "0":
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        loggedInUser = userService.login(username, password);

        if (loggedInUser != null) {
            System.out.println("Đăng nhập thành công! Xin chào " + loggedInUser.getFullName());
            System.out.println("Vai trò của bạn: " + loggedInUser.getRole());

            switch (loggedInUser.getRole()) {
                case "ADMIN":
                    AdminMenu.display();
                    break;
                case "EMPLOYEE":
                    EmployeeMenu.display();
                    break;
                case "SUPPORT":
                    SupportMenu.display();
                    break;
            }
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN NHÂN VIÊN ---");

        String username = ValidationUtil.getNonEmptyString(scanner, "Tên đăng nhập: ");
        String password = ValidationUtil.getNonEmptyString(scanner, "Mật khẩu: ");
        String fullName = ValidationUtil.getNonEmptyString(scanner, "Họ và tên: ");
        String dept = ValidationUtil.getNonEmptyString(scanner, "Phòng ban: ");

        // Dùng Regex để kiểm tra sđt và email
        String phone = ValidationUtil.getValidPhone(scanner, "Số điện thoại nội bộ (10 số): ");
        String email = ValidationUtil.getValidEmail(scanner, "Email: ");

        User newUser = new User(0, username, null, "EMPLOYEE", fullName, dept, phone, email);
        if (userService.registerEmployee(newUser, password)) {
            System.out.println("-> Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        } else {
            System.out.println("-> Đăng ký thất bại. Tên đăng nhập có thể đã tồn tại.");
        }
    }
}