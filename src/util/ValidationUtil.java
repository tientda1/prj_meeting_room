package util;

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
}