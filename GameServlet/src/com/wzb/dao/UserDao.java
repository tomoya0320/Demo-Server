package com.wzb.dao;
import com.wzb.entity.UserDB;

public interface UserDao {
	public boolean search(UserDB userData);
	public boolean insert(UserDB userData);
	public String getValue(String id, String name);
	public boolean updateValue(String account, String name, String value);
}
