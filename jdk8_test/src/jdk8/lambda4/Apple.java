package jdk8.lambda4;

import java.util.function.Predicate;

public class Apple {

	private String color;
	private int weight;
	private String name;
	private String grade;
	
	

	@Override
	public String toString() {
		return "Apple [color=" + color + ", weight=" + weight + ", name=" + name + ", grade=" + grade + "]";
	}

	public Apple() {
		super();
	}

	public Apple(String color, int weight, String name, String grade) {
		super();
		this.color = color;
		this.weight = weight;
		this.name = name;
		this.grade = grade;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


}
