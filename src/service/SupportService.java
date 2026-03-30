package service;

import dao.IBookingDAO;
import dao.impl.BookingDAOImpl;
import model.Booking;

import java.util.List;

public class SupportService {
    private final IBookingDAO bookingDAO = new BookingDAOImpl();

    public List<Booking> getAssignedTasks(int staffId) {
        return bookingDAO.getBookingsByStaffId(staffId);
    }

    public boolean updateTaskStatus(int bookingId, String newStatus) {
        if (!newStatus.equals("PREPARING") && !newStatus.equals("READY") && !newStatus.equals("LACK_EQUIPMENT")) {
            System.out.println("-> [LỖI] Trạng thái không hợp lệ!");
            return false;
        }
        return bookingDAO.updateBookingStatus(bookingId, newStatus);
    }
}