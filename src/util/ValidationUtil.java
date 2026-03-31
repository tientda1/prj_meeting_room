package util;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ValidationUtil {

    public static int getValidInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("-> [LỖI] Vui lòng nhập một số nguyên hợp lệ. Nhập lại: ");
            }
        }
    }

    public static LocalDateTime getValidDateTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            LocalDateTime dateTime = DateUtil.parse(input);
            if (dateTime != null) {
                return dateTime;
            }
            System.out.println("-> [LỖI] Sai định dạng. Vui lòng nhập theo mẫu: yyyy-M-dd H:mm (VD: 2026-4-15 8:30)");
        }
    }

    public static double getValidDouble(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("-> [LỖI] Vui lòng nhập một số hợp lệ (VD: 15000.0). Nhập lại: ");
            }
        }
    }

    // 1. Hàm bắt buộc nhập chuỗi (không được để rỗng)
    public static String getNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("-> [LỖI] Thông tin này không được để trống. Vui lòng nhập lại.");
        }
    }

    // 2. Hàm kiểm tra định dạng Email bằng Regex
    public static String getValidEmail(Scanner scanner, String prompt) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        while (true) {
            String email = getNonEmptyString(scanner, prompt);
            if (email.matches(regex)) {
                return email;
            }
            System.out.println("-> [LỖI] Email không hợp lệ (VD: conan@ptit.edu.vn).");
        }
    }

    // 3. Hàm kiểm tra định dạng Số điện thoại bằng Regex
    public static String getValidPhone(Scanner scanner, String prompt) {
        String regex = "^0\\d{9}$";
        while (true) {
            String phone = getNonEmptyString(scanner, prompt);
            if (phone.matches(regex)) {
                return phone;
            }
            System.out.println("-> [LỖI] Số điện thoại không hợp lệ (Bắt đầu bằng 0, gồm 10 số).");
        }
    }
}