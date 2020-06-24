package com.codegym.dao;

import com.codegym.model.Permission;
import com.codegym.model.User;

import java.util.List;

public interface IUserDAO {

    void insertUser(User user);

    User selectUser(int id);

    List<User> selectAllUsers();

    boolean deleteUser(int id);

    boolean updateUser(User user);

    List<User> searchAndSort(String type, String search, String sort);

    List<User> sortAllUsers(String sort);

    void addUserTransaction(User user, String[] permission);

	List<Permission> selectPermission(int id);
}
