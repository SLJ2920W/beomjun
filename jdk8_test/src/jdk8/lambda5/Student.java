package jdk8.lambda5;

public class Student {

	// public static final String MAN = "man";
	// public static final String WOMAN = "woman";

	public enum Gender {
		MAN, WOMAN
	}

	private String name;
	private int score;
	private Gender gender;

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Student(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	public Student(String name, int score, Gender gender) {
		super();
		this.name = name;
		this.score = score;
		this.gender = gender;
	}

	public Student() {
		super();
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", score=" + score + ", sex=" + gender + "]";
	}

}
