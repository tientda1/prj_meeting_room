package presentation;

import model.User;
import service.UserService;

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

            // Điều hướng menu dựa trên Role
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
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();
        System.out.print("Họ và tên: ");
        String fullName = scanner.nextLine();
        System.out.print("Phòng ban: ");
        String dept = scanner.nextLine();
        System.out.print("Số điện thoại nội bộ: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        User newUser = new User(0, username, null, "EMPLOYEE", fullName, dept, phone, email);
        if (userService.registerEmployee(newUser, password)) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        } else {
            System.out.println("Đăng ký thất bại. Tên đăng nhập có thể đã tồn tại.");
        }
    }
}