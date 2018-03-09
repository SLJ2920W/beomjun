package jdk8.objectpool;

public class MyPool {

	private int count;

	public MyPool() {
		count = count++;
	}

	public int getCount() {
		return count;
	}

}
