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
}