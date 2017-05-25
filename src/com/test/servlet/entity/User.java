package com.test.servlet.entity;

public class User {

	private long id;
	private String name;
	private String pass;
	private int level = 1;
	
	public User() {
	}

	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;
	}

	public User(String name, String pass, int level) {
		this.name = name;
		this.pass = pass;
		this.level = level;
	}

	public User(long id, String name, String pass, int level) {
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.level = level;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", pass=" + pass + ", level=" + level + "]";
	}
	
	
}
