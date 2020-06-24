package com.codegym.dao;

import com.codegym.model.Permission;
import com.codegym.service.DatabaseMamaner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO implements IPermissionDAO {
	protected static final String SELECT_ALL_PERMISSION = "SELECT * FROM `Permision`";

	protected Connection connection;

	public PermissionDAO() {
		connection = DatabaseMamaner.getInstance().getConnection();
	}

	@Override
	public List<Permission> selectAllPermission() {
		List<Permission> lists = new ArrayList<Permission>();

		try {
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(SELECT_ALL_PERMISSION);

			while(results.next()){
				int id      = results.getInt("id");
				String name = results.getString("name");

				Permission permission = new Permission();
				permission.setId(id);
				permission.setName(name);
				lists.add(permission);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return lists;
	}
}
