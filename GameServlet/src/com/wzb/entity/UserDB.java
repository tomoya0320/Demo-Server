package com.wzb.entity;

public class UserDB {
	public String account;
	public String password;
	public String items;
	public String tasks;
	
	public UserDB(String account, String password, String items, String tasks) {
		this.account = account;
		this.password = password;
		this.items = items;
		this.tasks = tasks;
	}
}