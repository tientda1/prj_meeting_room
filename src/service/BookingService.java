package service;

import dao.IBookingDAO;
import dao.impl.BookingDAOImpl;
import model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final IBookingDAO bookingDAO = new BookingDAOImpl();

    // 1. Lấy danh sách lịch sử đặt phòng
    public List<Booking> getMyBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    // 2. Kiểm tra xem phòng có trống không (Dùng để lọc ở Menu)
    public boolean isRoomAvailable(int roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDAO.isRoomAvailable(roomId, startTime, endTime);
    }

    // 3. Tạo Đơn đặt phòng
    public boolean createBooking(int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime,
                                 Map<Integer, Integer> equipments, Map<Integer, Integer> services) {

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);

        int newBookingId = bookingDAO.createBooking(booking);
        if (newBookingId != -1) {
            // Lưu thiết bị
            if (equipments != null && !equipments.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : equipments.entrySet()) {
                    bookingDAO.addBookingEquipment(newBookingId, entry.getKey(), entry.getValue());
                }
            }
            // Lưu dịch vụ
            if (services != null && !services.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : services.entrySet()) {
                    bookingDAO.addBookingService(newBookingId, entry.getKey(), entry.getValue());
                }
            }
            return true;
        }
        return false;
    }

    // 4. Hủy booking (Chỉ được hủy khi đang PENDING)
    public boolean cancelBooking(int bookingId, int userId) {
        // Kiểm tra xem booking có phải của user này không và có đang PENDING không
        List<Booking> myBookings = bookingDAO.getBookingsByUserId(userId);
        Booking target = myBookings.stream().filter(b -> b.getId() == bookingId).findFirst().orElse(null);

        if (target == null) {
            System.out.println("-> [LỖI] Không tìm thấy mã Đặt phòng này trong lịch sử của bạn.");
            return false;
        }
        if (!target.getStatus().equals("PENDING")) {
            System.out.println("-> [TỪ CHỐI] Chỉ có thể hủy Đơn đặt phòng khi đang ở trạng thái PENDING.");
            return false;
        }

        return bookingDAO.updateBookingStatus(bookingId, "REJECTED");
    }
}