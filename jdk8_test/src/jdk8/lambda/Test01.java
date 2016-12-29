package jdk8.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test01 {

	public static void main(String[] args) {
		List<String> myList = new ArrayList<String>();
		myList.add("naver");
		myList.add("google");
		myList.add("daum");
		myList.add("nate");
		myList.add("1");
		myList.add("01");
		myList.add(".");
		myList.add("facebook");
		
		
//		Collections.sort(myList, new Comparator<MyClass>() {
//			public int compare(MyClass a, MyClass b) {
//				return b.getValue() - a.getValue();
//			}
//		});
		
		
		Collections.sort(myList, String.CASE_INSENSITIVE_ORDER);
		System.out.println(myList);

		Collections.sort(myList, Collections.reverseOrder());
		
//		Collections.reverse(myList);
		System.out.println(myList);

		
		test$1();
	}
	
	public static void test$1(){
		System.out.println(123);
	}
	

}
