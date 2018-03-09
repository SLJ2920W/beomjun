package jdk8.test;

import java.io.IOException;
import java.util.Random;

public class RandomCreate {
	public static void main(String argc[]) throws IOException {
		int min = 4;
		int max = 20;
		int multiple = 4;
		min = min % multiple != 0 ? (min / multiple) * multiple : min;   
		max = max % multiple != 0 ? (max / multiple) * multiple : max;   
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			int s = Math.min(multiple * random.nextInt(max / multiple) + min, max);
			System.out.println(s);
		}
	}
}
