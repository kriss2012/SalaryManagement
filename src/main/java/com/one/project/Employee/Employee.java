package com.one.project.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employee {
	@Id
	private int id;
	
	private String name;
	private String designation;
	private double Salary;
	
	// Anime character enhancements & Credentials
	private String password;
	private String animeAlias;
	private String avatar; // e.g. "naruto", "luffy", "gojo", "nezuko"
	private int powerLevel;
	private String magicElement; // e.g. "Fire", "Lightning", "Shadow", "Spirit"
	private String specialAbility; // e.g. "Rasengan", "Domain Expansion"

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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getSalary() {
		return Salary;
	}

	public void setSalary(double salary) {
		Salary = salary;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAnimeAlias() {
		return animeAlias;
	}

	public void setAnimeAlias(String animeAlias) {
		this.animeAlias = animeAlias;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getPowerLevel() {
		return powerLevel;
	}

	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
	}

	public String getMagicElement() {
		return magicElement;
	}

	public void setMagicElement(String magicElement) {
		this.magicElement = magicElement;
	}

	public String getSpecialAbility() {
		return specialAbility;
	}

	public void setSpecialAbility(String specialAbility) {
		this.specialAbility = specialAbility;
	}

	public Employee(int id, String name, String designation, double salary, String password, String animeAlias, String avatar, int powerLevel, String magicElement, String specialAbility) {
		super();
		this.id = id;
		this.name = name;
		this.designation = designation;
		this.Salary = salary;
		this.password = password;
		this.animeAlias = animeAlias;
		this.avatar = avatar;
		this.powerLevel = powerLevel;
		this.magicElement = magicElement;
		this.specialAbility = specialAbility;
	}

	public Employee() {
		super();
	}
}
