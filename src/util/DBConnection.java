package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/prj_meeting_room?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // 2. Biến tĩnh lưu trữ kết nối duy nhất
    private static Connection connection;

    // 3. Private constructor để ngăn khởi tạo object từ bên ngoài (Singleton Pattern)
    private DBConnection() {
    }

    // 4. Hàm cấp phát kết nối
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[LỖI] Không tìm thấy thư viện MySQL JDBC Driver! Hãy kiểm tra lại file .jar.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[LỖI] Không thể kết nối tới Database! Kiểm tra lại URL, Username, Password hoặc xem MySQL đã chạy chưa.");
            e.printStackTrace();
        }
        return connection;
    }

    // 5. Hàm đóng kết nối (Dùng khi kết thúc chương trình)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[LỖI] Lỗi khi đóng kết nối Database.");
            e.printStackTrace();
        }
    }
}