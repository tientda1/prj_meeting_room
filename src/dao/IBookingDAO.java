package dao;

import model.Booking;
import java.time.LocalDateTime;
import java.util.List;

public interface IBookingDAO {
    boolean isRoomAvailable(int roomId, LocalDateTime startTime, LocalDateTime endTime);
    int createBooking(Booking booking);
    boolean addBookingEquipment(int bookingId, int equipmentId, int quantity);
    boolean addBookingService(int bookingId, int serviceId, int quantity);
    List<Booking> getBookingsByStatus(String status);
    List<Booking> getBookingsByUserId(int userId);
    List<Booking> getBookingsByStaffId(int staffId);
    boolean updateBookingStatus(int bookingId, String status);
    boolean assignSupportStaff(int bookingId, int staffId);
}