package jdk8.test;

public class PolymorphSize {

	static String[] capacityPostit = { "B", "KB", "MB", "GB", "TB"};
//	static String[] capacityPostit = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB", "VB", "RB", "OB", "QB", "XC" };

	public static void main(String[] args) {

		long size = 9627893;
		String result = polymorphSize(size);
		System.out.println("result 1 = " + result);
	}

	public static String polymorphSize(long size) {
		double temp = 0;
		int i = 0;
		for (i = 0; i < capacityPostit.length - 1; i++) {
			temp = (double) (size / Math.pow(1024, i + 1));
			if (temp < 1024) {
				temp = (double) (Math.floor(temp * 100d) / 100d);
				break;
			}
		}
		if (temp < 1) // byte 이하
			return size + "" + capacityPostit[i];
		else if (temp > 1024) // 변수 지정 한계 용량 이상
			return temp + "" + capacityPostit[i];
		else // 그외 KB,MB...등
			return temp + "" + capacityPostit[i + 1];
	}


}