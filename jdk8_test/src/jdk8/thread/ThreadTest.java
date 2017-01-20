package jdk8.thread;

import java.util.Set;

public class ThreadTest {
	public static void main(String args[]) throws Exception {
		Thread.currentThread().setName("ThreadSet");
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new MyThread());
			t.setName("MyThread:" + i);
			t.start();
		}
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for (Thread t : threadSet) {
			if (t.getThreadGroup() == Thread.currentThread().getThreadGroup()) {
				System.out.println("Thread :" + t + ":" + "state:" + t.getState());
			}
		}
	}
}

class MyThread implements Runnable {
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}
