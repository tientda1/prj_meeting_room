package dao;
import model.Room;
import java.util.List;

public interface IRoomDAO {
    List<Room> getAllRooms();
    boolean addRoom(Room room);
    boolean updateRoom(Room room);
    boolean deleteRoom(int id);
}