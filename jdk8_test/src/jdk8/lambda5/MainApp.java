package jdk8.lambda5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainApp {

	static int sum = 0;

	public static void main(String[] args) {
		// [s] stream
		List<String> list1 = Arrays.asList("참외", "딸기", "수박", "사과", "포도");

		list1.forEach(name -> System.out.print(name + " "));

		System.out.println();

		Stream<String> list2 = list1.stream();
		list2.forEach(name -> System.out.print(name + " "));

		System.out.println();

		// String flag = args.length > 0 ? args[0] : null;
		// if (flag != null) {
		Stream<String> list3 = list1.parallelStream();
		list3.forEach(name -> print(name));

		System.out.println();

		Stream<String> list4 = list1.parallelStream();
		list4.forEach(MainApp::print);
		System.out.println();
		// }
		// [e] stream

		// [s] pipeline
		List<Student> list5 = Arrays.asList(new Student("park", 80), new Student("kim", 82), new Student("lee", 90),
				new Student("jung", 65), new Student("hong", 22));

		list5.sort((a, b) -> b.getScore() - a.getScore());
		list5.forEach(a -> System.out.println(a.getName() + ", " + a.getScore()));

		double average = list5.stream().mapToInt(Student::getScore).average().getAsDouble();
		System.out.println(average);

		IntStream s = IntStream.rangeClosed(1, 10);
		s.forEach(number -> sum += number);
		System.out.println(sum);
		// [e] pipeline
		

	}

	public static void print(String str) {
		System.out.println(str + " : " + Thread.currentThread().getName());
	}

}
