package com.codegym.dao;

import com.codegym.model.Permission;
import com.codegym.model.User;
import com.codegym.service.DatabaseMamaner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    protected static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";

    protected static final String SELECT_USER_BY_ID = "SELECT id,name,email,country FROM users WHERE id =?";

    protected static final String SELECT_ALL_USERS  = "SELECT * FROM users";
    protected static final String SELECT_USER_PERMISSION = "SELECT * FROM `User_Permision` WHERE user_id = ?";

    protected static final String DELETE_USERS_SQL  = "delete FROM users WHERE id = ?;";
    protected static final String UPDATE_USERS_SQL  = "update users SET name = ?,email= ?, country =? WHERE id = ?;";

    protected Connection connection;

    public UserDAO() {
        connection = DatabaseMamaner.getInstance().getConnection();
    }

    public void insertUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        // Step 1: Establishing a Connection
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs);

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public List<User> sortAllUsers(String sort) {
        if (!sort.equals("id") && !sort.equals("name") && !sort.equals("email") && !sort.equals("country")){
            sort = "id";
        }
        String SORT_ALL_USERS = "SELECT * FROM users ORDER BY `"+sort+"` ASC";
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            System.out.println(statement);
            ResultSet rs = statement.executeQuery(SORT_ALL_USERS);
            System.out.println(rs);
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public void addUserTransaction(User user, String[] permissions) {
        PreparedStatement pstmt           = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs                      = null;

        try {
            connection.setAutoCommit(false);
            // assign permision to user
            String sqlPivot = "INSERT INTO user_permision(user_id, permision_id) VALUES(?, ?)";
            pstmtAssignment = connection.prepareStatement(sqlPivot);
            for (String permisionId: permissions) {
                pstmtAssignment.setInt(1, user.getId());
                pstmtAssignment.setString(2, permisionId);
                pstmtAssignment.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (pstmtAssignment != null) pstmtAssignment.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Permission permissionByID(int id) {
        Permission permisson = new Permission();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `name` FROM `Permision` WHERE id = ?");
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            if (results.next()){
                String name = results.getString("name");
                permisson.setName(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return permisson;
    }

    @Override
    public List<Permission> selectPermission(int id) {
        List<Permission> listsPermisson = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_PERMISSION);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            while (results.next()){
                int idPermisson = results.getInt("permision_id");
                Permission permission = permissionByID(idPermisson);
                listsPermisson.add(permission);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listsPermisson;
    }

    public List<User> searchAndSort(String type, String search, String sort) {

        if (!sort.equals("id") && !sort.equals("name") && !sort.equals("email") && !sort.equals("country")){
            sort = "id";
        }

        if (!type.equals("name") && !type.equals("country")){
            type = "country";
        }

        String query = "SELECT * FROM `users` WHERE `"+type+"` LIKE ? ORDER BY `"+sort+"` ASC;";

        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE_USERS_SQL);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_USERS_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
