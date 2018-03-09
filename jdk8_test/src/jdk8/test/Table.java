package jdk8.test;

import java.util.Scanner;

public class Table {
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		System.out.print("행의 수: ");
		int rows = input.nextInt();
		System.out.print("열의 수: ");
		int cols = input.nextInt();
		System.out.print("난수의 최대값: ");
		int max = input.nextInt();

		int[][] table = new int[rows + 1][cols + 1];

		System.out.println("초기 배열");
		display(table, rows, cols);

		System.out.println("난수가 저장된 배열");
		random(table, rows, cols, max);
		display(table, rows, cols);

		System.out.println("가로, 세로 합이 계산된 배열");
		total(table);
		display(table, rows + 1, cols + 1);

		input.close();

	}

	/**
	 * 이차원 배열 table의 첫 rwos 행, 첫 cols 열에 있는 원소들을 표 형태로 출력한다. 전제조건: rows는 table의
	 * 행 개수보다 작거나 같고, coss는 table의 열 개수보다 작거나 같다.
	 * 
	 * @param table 아치원 정수 배열
	 * @param rows 출력할 행 수
	 * @param cols 출력할 열 수
	 */
	public static void display(int[][] table, int rows, int cols) {

		for (int j = 0; j < rows; j++) {
			for (int s = 0; s < cols; s++) {
				System.out.print(table[j][s] + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * 2차원 배열 table의 전부 혹은 일부에 난수를 채운다. 난수는 0이상, max "이하" 정수이어야 한다. 전제 조건: rows는
	 * table의 행 개수보다 작거나 같아야 한다. 전제 조건: cols는 table의 열 개수보다 작거나 같아야 한다.
	 * 
	 * @param table 배열
	 * @param rows 난수를 채울 행 개수
	 * @param cols 난수를 채울 열 개수
	 * @param max 난수의 최대값
	 */
	public static void random(int[][] table, int rows, int cols, int max) {

		for (int j = 0; j < rows; j++) {
			for (int s = 0; s < cols; s++) {
				table[j][s] = (int) (Math.random() * max);
			}

		}
	}

	/**
	 * 2차원 배열 a의 마지막 행과 마지막 열에 세로합과 가로합을 채운다. a의 마지막 행과 마지막 열을 제외한 나머지 셀들에 데이터가
	 * 들어 있다.
	 * 
	 * @param array 2차원 배열
	 */
	public static void total(int[][] array) {

		int hSum = 0, vSum = 0, sum = 0;
		for (int vi = 0; vi < array.length; vi++) {
			for (int hi = 0; hi < array[vi].length; hi++) {
				hSum += array[vi][hi];
			}
			array[vi][array[0].length - 1] = hSum;
			hSum = 0;
			sum += hSum;
		}

		for (int vi = 0; vi < array[0].length; vi++) {
			vSum = 0;
			for (int hi = 0; hi < array.length - 1; hi++) {
				vSum += array[hi][vi];
			}
			array[array.length - 1][vi] = vSum;
			sum += vSum;
		}
		array[array.length - 1][array[0].length - 1] = sum; 
		
		
	}
}

