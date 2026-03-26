package model;

public class Service {
    private int id;
    private String serviceName;
    private double unitPrice;

    public Service() {
    }

    public Service(int id, String serviceName, double unitPrice) {
        this.id = id;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Service{" + "id=" + id + ", name='" + serviceName + '\'' + ", price=" + unitPrice + '}';
    }
}