package jdk8.thread.t0;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadTest {

	public static void main(String[] args) {

		BlockingQueue<Path> pathQueue = new ArrayBlockingQueue<>(1);

		final Path myPath = Paths.get("D:\\Hanwha\\메일백업\\test");

		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				try {
					Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							try {
								pathQueue.put(file);
								System.out.println("input = " + file.toString());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							return FileVisitResult.CONTINUE;
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				while (true) {
					Path p;
					try {
						p = pathQueue.take();
						if ("".equals(p.toString())) {
							break;
						}
						System.out.println("output = " + p);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();

	}

}
