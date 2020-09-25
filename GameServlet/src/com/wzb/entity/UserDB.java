package com.wzb.entity;

public class UserDB {
	public String name;
	public String account;
	public String password;
	public String items;
	public String tasks;
	public String mails;

	public UserDB() {
	}

	public UserDB(String name, String account, String password, String items, String tasks, String mails) {
		this.name = name;
		this.account = account;
		this.password = password;
		this.items = items;
		this.tasks = tasks;
		this.mails = mails;
	}
}
