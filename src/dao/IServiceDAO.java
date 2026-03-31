package dao;

import model.Service;
import java.util.List;

public interface IServiceDAO {
    List<Service> getAllServices();
    boolean addService(Service service);
    boolean updateService(Service service);
    boolean deleteService(int id);
}