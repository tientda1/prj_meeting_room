package dao;
import model.User;

public interface IUserDAO {
    User login(String username, String hashedPassword);
    boolean registerEmployee(User user);
    boolean createStaffAccount(User user);
}