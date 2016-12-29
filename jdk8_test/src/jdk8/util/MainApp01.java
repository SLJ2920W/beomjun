package jdk8.util;

import java.util.UUID;

public class MainApp01 {

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			sb.append(UUID.randomUUID().toString());
		}
		
		

	}

}
