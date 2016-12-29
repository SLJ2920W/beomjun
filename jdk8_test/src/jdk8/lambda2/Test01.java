package jdk8.lambda2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test01 {

	public static void main(String[] args) {
		Actor[] array = new Actor[] { new Actor("이름A", 24, "영화A"), new Actor("이름B", 46, "영화B"),
				new Actor("이름C", 32, "영화C"), new Actor("이름D", 19, "영화D") };
		// Arrays.sort(a);
//		print("기본 데이터", array);
//
		List<Actor> list = Arrays.asList(array);
//		print("배열에서 리스트로", list);

		Collections.shuffle(list);
		print("섞기", list);

		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Actor a = (Actor) o1;
				Actor b = (Actor) o2;
				return a.getName().compareTo(b.getName());
			}
		});

		print("정렬", list);

		
		Collections.shuffle(list);
		print("섞기", list);
		
//		list.sort((a, b) -> a.getAge() - b.getAge());
		list.sort((a, b) -> a.getName().compareTo(b.getName()));

		print("정렬", list);
		
	}

	public static void print(String text, Actor[] array) {
		System.out.println(text);
		for (Actor a : array) {
			System.out.println(a.toString());
		}
		System.out.println();

	}

	public static void print(String text, List<Actor> list) {
		System.out.println(text);
		list.forEach(i -> System.out.println(i));
		System.out.println();
	}

}
