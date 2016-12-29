package jdk8.lambda4;

import java.util.function.Predicate;

public class Orange {

	private String color;
	private int weight;
	private String name;
	private String grade;

	public Orange() {
		super();
	}

	public Orange(String color, int weight, String name, String grade) {
		super();
		this.color = color;
		this.weight = weight;
		this.name = name;
		this.grade = grade;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


}
