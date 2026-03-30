package service;

import dao.IBookingDAO;
import dao.impl.BookingDAOImpl;
import model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final IBookingDAO bookingDAO = new BookingDAOImpl();

    public boolean createBooking(int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime, Map<Integer, Integer> equipments) {
        // 1. Kiểm tra thời gian hợp lệ (Không đặt trong quá khứ)
        if (startTime.isBefore(LocalDateTime.now())) {
            System.out.println("-> [TỪ CHỐI] Thời gian bắt đầu không được nhỏ hơn thời gian hiện tại.");
            return false;
        }

        // 2. Kiểm tra logic bắt đầu & kết thúc
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            System.out.println("-> [TỪ CHỐI] Thời gian kết thúc phải sau thời gian bắt đầu.");
            return false;
        }

        // 3. Kiểm tra trùng lịch (Gọi DAO để check trong CSDL)
        if (!bookingDAO.isRoomAvailable(roomId, startTime, endTime)) {
            System.out.println("-> [XUNG ĐỘT] Phòng này đã có người đặt trong khoảng thời gian trên. Vui lòng chọn giờ hoặc phòng khác.");
            return false;
        }

        // 4. Nếu mọi thứ hợp lệ, tiến hành tạo Booking với trạng thái PENDING
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        // Trạng thái mặc định đã được set trong DAO hoặc DB là PENDING

        int newBookingId = bookingDAO.createBooking(booking);
        if (newBookingId != -1) {
            // 5. Lưu danh sách thiết bị mượn kèm (nếu có)
            if (equipments != null && !equipments.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : equipments.entrySet()) {
                    int eqId = entry.getKey();
                    int qty = entry.getValue();
                    bookingDAO.addBookingEquipment(newBookingId, eqId, qty);
                }
            }
            return true;
        }

        System.out.println("-> [LỖI] Không thể lưu đơn đặt phòng vào cơ sở dữ liệu.");
        return false;
    }
    public List<Booking> getMyBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }
}