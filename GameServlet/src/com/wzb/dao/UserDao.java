package com.wzb.dao;
import com.wzb.entity.UserDB;

public interface UserDao {
	public boolean search(String account);
	public boolean insert(UserDB userDB);
	public String getValue(String account, String name);
	public boolean updateValue(String account, String name, String value);
}
