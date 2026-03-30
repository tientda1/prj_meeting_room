package dao;
import model.User;

import java.util.List;

public interface IUserDAO {
    User login(String username, String hashedPassword);
    boolean registerEmployee(User user);
    boolean createStaffAccount(User user);
    List<User> getUsersByRole(String role);
    boolean updateProfile(User user);
}