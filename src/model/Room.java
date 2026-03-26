package model;

public class Room {
    private int id;
    private String roomName;
    private int capacity;
    private String location;
    private String fixedEquipments;

    public Room() {
    }

    public Room(int id, String roomName, int capacity, String location, String fixedEquipments) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.location = location;
        this.fixedEquipments = fixedEquipments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFixedEquipments() {
        return fixedEquipments;
    }

    public void setFixedEquipments(String fixedEquipments) {
        this.fixedEquipments = fixedEquipments;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name='" + roomName + '\'' + ", capacity=" + capacity +
                ", location='" + location + '\'' + '}';
    }
}