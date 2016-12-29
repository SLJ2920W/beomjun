package jdk8.lambda2;

import java.util.Comparator;

public class Actor implements Comparator<Object> {

	private String name;
	private int age;
	private String movie;

	public Actor() {
		super();
	}

	public Actor(String name, int age, String movie) {
		super();
		this.name = name;
		this.age = age;
		this.movie = movie;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	@Override
	public String toString() {
		return "Actor [name=" + name + ", age=" + age + ", movie=" + movie + "]";
	}

	@Override
	public int compare(Object o1, Object o2) {
		return 0;
	}

}
