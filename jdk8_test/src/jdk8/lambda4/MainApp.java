package jdk8.lambda4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainApp {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {

		// [s] stream test
		List<Apple> list = new ArrayList<Apple>();
		list.add(new Apple("red", 30, "aApple", "A"));
		list.add(new Apple("yellow", 15, "bApple", "E"));
		list.add(new Apple("green", 20, "cApple", "D"));
		list.add(new Apple("black", 25, "dApple", "B"));
		list.add(new Apple("purple", 10, "eApple", "C"));

		list = filterApples(list, (Apple apple) -> "red".equals(apple.getColor()));

		System.out.println(list);
		// [e] stream test

		// [s] thread test
		long start = System.nanoTime();
		System.out.println(Thread.currentThread().getName() + ": RunnableTest");
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " : is Running");
			}
		});

		Runnable task2 = new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " : is Running");
			}
		};

		Runnable task3 = () -> {
			System.out.println(Thread.currentThread().getName() + " : is Running");
		};

		Thread thread2 = new Thread(task2);
		Thread thread3 = new Thread(task3);
		thread1.start();
		thread2.start();
		thread2.sleep(2000);
		thread3.start();
		long end = System.nanoTime();

		System.out.println("time interval : " + (end - start) / 1000000);

		// [e] thread test
	}

	public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
		return inventory.stream().filter(t -> p.test(t)).collect(Collectors.toList());
	}

}
