package com.codegym.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseMamaner {
	private String jdbcURL      = "jdbc:mysql://localhost:3306/testjdbc?useSSl=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private String jdbcUsername = "root";
	private String jdbcPassword = "";

	private Connection connection;
	private static DatabaseMamaner instance;

	private DatabaseMamaner() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			System.out.println("ok connecting...");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static DatabaseMamaner getInstance() {
		if (instance == null){
			synchronized (DatabaseMamaner.class){
				instance = new DatabaseMamaner();
			}
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}
