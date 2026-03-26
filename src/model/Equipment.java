package model;

public class Equipment {
    private int id;
    private String equipmentName;
    private int totalQuantity;
    private int availableQuantity;
    private String status;

    public Equipment() {
    }

    public Equipment(int id, String equipmentName, int totalQuantity, int availableQuantity, String status) {
        this.id = id;
        this.equipmentName = equipmentName;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Equipment{" + "id=" + id + ", name='" + equipmentName + '\'' +
                ", available=" + availableQuantity + "/" + totalQuantity + '}';
    }
}