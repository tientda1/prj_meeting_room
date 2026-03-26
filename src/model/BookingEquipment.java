package model;

public class BookingEquipment {
    private int bookingId;
    private int equipmentId;
    private int quantity;

    public BookingEquipment() {
    }

    public BookingEquipment(int bookingId, int equipmentId, int quantity) {
        this.bookingId = bookingId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
