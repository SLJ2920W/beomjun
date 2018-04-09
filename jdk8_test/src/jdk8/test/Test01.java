package jdk8.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Test01 {

	public static void main(String[] args) throws UnsupportedEncodingException {

		byte[] asBytes = Base64.getDecoder().decode("2ZiHGvzlvMA8D6GZL3IJbdUJTvgAjLErA/r2UgkJWpg=");
		System.out.println(new String(asBytes));
		
		
		
		
		System.out.println("테스트......");
		
	}

}
