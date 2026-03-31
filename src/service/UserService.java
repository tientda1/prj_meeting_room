package service;

import dao.IUserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import util.PasswordHash;

public class UserService {
    private final IUserDAO userDAO = new UserDAOImpl();

    public User login(String username, String rawPassword) {
        String hashedPassword = PasswordHash.hashPassword(rawPassword);
        return userDAO.login(username, hashedPassword);
    }

    public boolean registerEmployee(User user, String rawPassword) {
        user.setPassword(PasswordHash.hashPassword(rawPassword));
        return userDAO.registerEmployee(user);
    }

    public boolean updateProfile(User user) {
        return userDAO.updateProfile(user);
    }
}
