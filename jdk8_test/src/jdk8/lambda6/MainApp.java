package jdk8.lambda6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jdk8.lambda5.Student;

public class MainApp {

	public static void main(String[] args) {

		int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		boolean allMatchFlag = Arrays.stream(array).allMatch(i -> i / 2 == 0);
		boolean anyMatchFlag = Arrays.stream(array).anyMatch(i -> i / 2 == 0);
		boolean noneMatchFlag = Arrays.stream(array).noneMatch(i -> i / 10 == 0);

		System.out.println(allMatchFlag);
		System.out.println(anyMatchFlag);
		System.out.println(noneMatchFlag);

		Arrays.stream(array).peek(System.out::print); // do not run
		int sum = Arrays.stream(array).peek(System.out::print).sum();
		System.out.println("\nsum : " + sum);

		List<Integer> emptyList = new ArrayList<>();
		// emptyList.add(100);
		OptionalDouble emptyFlag = emptyList.stream().mapToInt(Integer::intValue).average();
		if (emptyFlag.isPresent()) {
			System.out.println("fully ");
		} else {
			System.out.println("empty ");
		}

		emptyList = new ArrayList<>();
		double average = emptyList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
		System.out.println("average : " + average);

		emptyList = new ArrayList<>();
		// emptyList.add(60);
		// emptyList.add(20);
		// emptyList.add(40);
		emptyList.stream().mapToInt(Integer::intValue).average().ifPresent(e -> System.out.println("this value : " + e));

		List<Student> list = Arrays.asList(new Student("이순신", 70, Student.Gender.WOMAN), new Student("홍길동", 36, Student.Gender.MAN), new Student("세종대왕", 92, Student.Gender.MAN),
				new Student("홍국영", 23, Student.Gender.MAN), new Student("만적", 40, Student.Gender.WOMAN), new Student("이이", 56, Student.Gender.WOMAN),
				new Student("이황", 26, Student.Gender.WOMAN), new Student("안중근", 86, Student.Gender.MAN));

		int result = list.stream().map(Student::getScore).reduce((a, b) -> a + b).get();
		System.out.println("reduce result : " + result);

		Collectors.toList();

		// ------------------------------------------------------------------------------------------

		Stream<Student> totalStream = list.stream();
		Stream<Student> maleStream = totalStream.filter(s -> s.getGender() == Student.Gender.MAN);
		Collector<Student, ?, List<Student>> collector = Collectors.toList();
		List<Student> maleList = maleStream.collect(collector);
		maleList.stream().forEach(System.out::println);

		List<Student> list2 = list.stream().filter(s -> s.getGender() == Student.Gender.MAN).collect(Collectors.toList());
		System.out.println(list2);
		
		System.out.println("a".hashCode());
		System.out.println("b".hashCode());
		System.out.println("c".hashCode());
		
		System.exit(1);

	}

}
