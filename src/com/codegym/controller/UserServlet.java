package com.codegym.controller;

import com.codegym.dao.*;
import com.codegym.model.Permission;
import com.codegym.model.User;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserDAO userDAO;
    private IPermissionDAO permissionDAO;

    public void init() {
        userDAO       = new UserStoredDAO();
        permissionDAO = new PermissionDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "permission":
                addUserPermission(request, response);
                break;
                
            case "create":
                try {
                    insertUser(request, response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "edit":
                try {
                    updateUser(request, response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
        }
    }

    private void addUserPermission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        User existingUser   = userDAO.selectUser(id);
        String[] checkedIds = request.getParameterValues("permission[]");

        userDAO.addUserTransaction(existingUser, checkedIds);

        List<Permission> listUserPermission = userDAO.selectPermission(id);

        List<Permission> listPermission = permissionDAO.selectAllPermission();

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/permission.jsp");
        request.setAttribute("user", existingUser);
        request.setAttribute("userPermissions", listUserPermission);
        request.setAttribute("permissions", listPermission);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "permission":
                showManaerUserPermission(request, response);
                break;
                
            case "create":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                try {
                    deleteUser(request, response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            default:
                listUser(request, response);
                break;
        }
    }

    private void showManaerUserPermission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        User existingUser                   = userDAO.selectUser(id);
        List<Permission> listUserPermission = userDAO.selectPermission(id);

        for (int i = 0; i < listUserPermission.size(); i++){
            String name = listUserPermission.get(i).getName();

            System.out.println(name);
        }

        List<Permission> listPermission = permissionDAO.selectAllPermission();

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/permission.jsp");
        request.setAttribute("user", existingUser);
        request.setAttribute("userPermissions", listUserPermission);
        request.setAttribute("permissions", listPermission);
        dispatcher.forward(request, response);
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");

        List<User> listUser;

        String search = request.getParameter("search");
        String sort   = request.getParameter("sort");
        String type   = request.getParameter("type");

        if (sort == null){
            sort = "id";
        }

        if (type == null){
            type = "name";
        }

        if (search != null || sort != null){
            if (search == null){
                listUser = userDAO.sortAllUsers(sort);
            }else{
                search = URLDecoder.decode(search);
                listUser = userDAO.searchAndSort(type, search, sort);
            }
        }else{
            listUser = userDAO.selectAllUsers();
        }

        if (search == null){
            search = "";
        }

        request.setAttribute("search", search);
        request.setAttribute("sort",   sort);
        request.setAttribute("type",   type);

        request.setAttribute("listUser", listUser);
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User newUser = new User(name, email, country);
        userDAO.insertUser(newUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User book = new User(id, name, email, country);
        userDAO.updateUser(book);
        request.setAttribute("user", book);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect("/users");
    }
}
