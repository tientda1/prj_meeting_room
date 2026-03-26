package model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer supportStaffId;
    private LocalDateTime createdAt;

    public Booking() {
    }

    public Booking(int id, int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime, String status, Integer supportStaffId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.supportStaffId = supportStaffId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSupportStaffId() {
        return supportStaffId;
    }

    public void setSupportStaffId(Integer supportStaffId) {
        this.supportStaffId = supportStaffId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Booking{" + "id=" + id + ", userId=" + userId + ", roomId=" + roomId +
                ", startTime=" + startTime + ", endTime=" + endTime + ", status='" + status + '\'' + '}';
    }
}