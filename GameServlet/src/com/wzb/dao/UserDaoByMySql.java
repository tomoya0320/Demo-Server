package com.wzb.dao;

import java.sql.*;
import com.wzb.dao.UserDao;
import com.wzb.entity.UserDB;

public class UserDaoByMySql implements UserDao {
	// MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/wzb?useSSL=false&serverTimezone=UTC";
	// 数据库的用户名与密码，需要根据自己的设置
	static final String USER = "root";
	static final String PASS = "19980320";

	static {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}

	public void closeAll(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	@Override
	public boolean search(UserDB userData) {
		// TODO Auto-generated method stub
		boolean result = false;
		String sql = "select * from user where account='" + userData.account + "'";
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			result = rs.next();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public boolean insert(UserDB userData) {
		// TODO Auto-generated method stub
		String sql = "insert into user(account, password) values(?,?)";
		try {
			PreparedStatement preStmt = getConnection().prepareStatement(sql);
			preStmt.setString(1, userData.account);
			preStmt.setString(2, userData.password);
			preStmt.execute();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public String getValue(String id, String name) {
		String value = "";
		String sql = "select " + name + " from user where account='" + id + "'";
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				value = rs.getString(name);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

	@Override
	public boolean updateValue(String account, String name, String value) {
		// TODO Auto-generated method stub
		String sql = "update user set " + name + "=? where account=?";
		try {
			PreparedStatement preStmt = getConnection().prepareStatement(sql);
			preStmt.setString(1, value);
			preStmt.setString(2, account);
			preStmt.execute();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return false;
	}

}
