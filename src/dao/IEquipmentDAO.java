package dao;
import model.Equipment;
import java.util.List;

public interface IEquipmentDAO {
    List<Equipment> getAllEquipments();
    boolean addEquipment(Equipment eq);
    boolean updateAvailableQuantity(int equipmentId, int availableQuantity);
    boolean updateEquipment(Equipment equipment);
    boolean deleteEquipment(int id);
}
