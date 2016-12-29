package jdk8.lambda5;

public enum Grade {

	GradeA("90"), GradeB("80");

	private String grade;

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	private Grade(String grade) {
		this.grade = grade;
	}
	
	

}
