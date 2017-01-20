package jdk8.lambda6;

public enum Test {
	
	HO("1"),HO2("2"),HO3("33");

	private String test;

	private Test(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
