package com.codegym.model;

public class Permission {
	protected int    id;
	protected String name;

	public Permission(String name){
		this.name = name;
	}

	public Permission(int id, String name){
		this.id   = id;
		this.name = name;
	}

	public Permission() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
