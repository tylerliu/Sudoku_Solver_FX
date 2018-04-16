package Generator;

import java.util.Random;

import Solvers.DLXs99;
import Solvers.Solver;

public class ResultGen {

	private static Random gen = new Random();//random generator
	
	/**
	 * generate a solved sudoku grid
	 * @return a result sudoku grid generated
	 */
	public static int[][] genResult() {
		int [][] result; //original
		do {
			result = las_vagas();
		} while (result == null);
		transform(result);
		
		return result;

	}
		
	
	public static void transform(int[][] result){
		//swap numbers
		int[] swap = swapGen();
		for (int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j ++){
				if (result[i][j] == 0)continue;
				result[i][j] = swap[result[i][j]-1]+1;
			}
		
		int ranr;
		//rotate
		ranr = gen.nextInt(4);
		for(int i = 0; i < ranr; i++) rotate(result);
		//swap matrix lines
		ranr =  gen.nextInt(2);
		if (ranr == 1) Vswap(result);
		ranr =  gen.nextInt(2);
		if (ranr == 1) Hswap(result);
		
		for (int i = 0; i < 3; i ++){
			ranr =  gen.nextInt(6);
			change(result,ranr+6*i);
			ranr =  gen.nextInt(6);
			change(result,ranr+18+6*i);
		}
		
		ranr =  gen.nextInt(6);
		change(result,ranr+36);
		ranr =  gen.nextInt(6);
		change(result,ranr+36+6);
			
	}
	
	/**
	 * Generates number swapping
	 * @author Tyler Liu
	 * @return the array for number swapping
	 */

	public static int[] swapGen(){
		boolean[] used = new boolean[9];
		int ans[] = new int[9];
		for (int i = 0; i < 9; i ++){
			used[i] = false;
		}
		int seed = gen.nextInt(factorial(9)); 
		for (int i = 8; i >= 0; i --){//each digit
			int k = seed / factorial(i);
			seed = seed %factorial(i);
			for (int j = 0; j <= k; j++){
				if (used[j])k++;
			}
			used[k] = true;
			ans[i] = k;
		}
		return ans;
	}
	
	/**
	 * calculate n factorial
	 * @Precondition 0 <= n <= 10
	 * @param n the initial number
	 * @return  n! 
	 */
	public static int factorial(int n){
		if (n <= 1)return 1;
		return n*factorial(n-1);
	}
	
	/**
	 * from 0-17 swap rows in the block, 18-35 swap columns in the block, 36-41 swap rows of block, 42- swap columns of block
	 * @param matrix
	 * @param change 0-48 select the change
	 */
	public static void change (int[][] matrix, int change) {
		if (change < 18){//swap rows, 0-8
			switch (change % 6){
			case 0:
				break;
			case 1:
				//swap row
				swapRow(matrix, change/6*3 , change/6*3 +1);
				break;
			case 2:
				swapRow(matrix, change/6*3 , change/6*3 +2);
				break;
			case 3:
				swapRow(matrix, change/6*3 + 2 , change/6*3 +1);
				break;
			case 4:
				swapRow(matrix, change/6*3 , change/6*3 +1);
				swapRow(matrix, change/6*3 , change/6*3 +2);
				break;
			case 5:
				swapRow(matrix, change/6*3 , change/6*3 +2);
				swapRow(matrix, change/6*3 + 2 , change/6*3 +1);
				break;
			}
			return;
		}
		change = change - 18;
		if (change < 18){//swap Cols, 0-8
			switch (change % 6){
			case 1:
				//swap cols
				swapCol(matrix, change/6*3 , change/6*3 +1);
				break;
			case 2:
				swapCol(matrix, change/6*3 , change/6*3 +2);
				break;
			case 3:
				swapCol(matrix, change/6*3 + 2 , change/6*3 +1);
				break;
			case 4:
				swapCol(matrix, change/6*3 , change/6*3 +1);
				swapCol(matrix, change/6*3 , change/6*3 +2);
				break;
			case 5:
				swapCol(matrix, change/6*3 , change/6*3 +2);
				swapCol(matrix, change/6*3 + 2 , change/6*3 +1);
				break;
			}
			return;
		}
		change = change - 18;
		if (change < 18){//swap squares
			switch (change){
			case 0:
				break;
			case 1:
				//swap rows
				swapRow(matrix, 0 , 3);
				swapRow(matrix, 1 , 4);
				swapRow(matrix, 2 , 5);

				break;
			case 2:
				swapRow(matrix, 0 , 6);
				swapRow(matrix, 1 , 7);
				swapRow(matrix, 2 , 8);
				break;
			case 3:
				swapRow(matrix, 3 , 6);
				swapRow(matrix, 4 , 7);
				swapRow(matrix, 5 , 8);
				break;
			case 4:
				swapRow(matrix, 0 , 3);
				swapRow(matrix, 1 , 4);
				swapRow(matrix, 2 , 5);
				swapRow(matrix, 0 , 6);
				swapRow(matrix, 1 , 7);
				swapRow(matrix, 2 , 8);
			case 5:
				swapRow(matrix, 0 , 6);
				swapRow(matrix, 1 , 7);
				swapRow(matrix, 2 , 8);
				swapRow(matrix, 3 , 6);
				swapRow(matrix, 4 , 7);
				swapRow(matrix, 5 , 8);
			case 6:
				break;
			case 7:
				//swap rows
				swapCol(matrix, 0 , 3);
				swapCol(matrix, 1 , 4);
				swapCol(matrix, 2 , 5);

				break;
			case 8:
				swapCol(matrix, 0 , 6);
				swapCol(matrix, 1 , 7);
				swapCol(matrix, 2 , 8);
				break;
			case 9:
				swapCol(matrix, 3 , 6);
				swapCol(matrix, 4 , 7);
				swapCol(matrix, 5 , 8);
				break;
			case 10:
				swapCol(matrix, 0 , 3);
				swapCol(matrix, 1 , 4);
				swapCol(matrix, 2 , 5);
				swapCol(matrix, 0 , 6);
				swapCol(matrix, 1 , 7);
				swapCol(matrix, 2 , 8);
			case 11:
				swapCol(matrix, 0 , 6);
				swapCol(matrix, 1 , 7);
				swapCol(matrix, 2 , 8);
				swapCol(matrix, 3 , 6);
				swapCol(matrix, 4 , 7);
				swapCol(matrix, 5 , 8);			}
			return;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		long time = System.nanoTime();
		int[][] result = genResult();
		System.out.println(System.nanoTime() - time);
		
		System.out.println("|-----|-----|-----|");
	    for(int i=0;i<=8;i++)
	    {
	        System.out.print("|");
	        for(int j=0;j<=8;j++)
	        {
	            System.out.print(result[i][j]);
	            if(j == 2 || j == 5|| j==8 ) System.out.print("|");
	            else System.out.print(" ");
	        }
	        System.out.println();
	        if (i ==2 || i== 5) System.out.println("|-----|-----|-----|");
	        if (i== 8) System.out.println("|-----|-----|-----|");
	    }
	}
	
	/**
	 * @param matrix the sudoku matrix
	 * @param index1 index of swapped line
	 * @param index2 index of swapped line
	 */
	public static void swapRow(int[][] matrix, int index1, int index2){
		int[] temp = matrix[index1];
		matrix[index1] = matrix[index2];
		matrix[index2] = temp;
	}
	
	/**
	 * @param matrix the sudoku matrix
	 * @param index1 index of swapped column
	 * @param index2 index of swapped column
	 */
	public static void swapCol(int[][] matrix, int index1, int index2){
		int temp;
		for (int i =0; i < matrix[0].length;i ++){
			temp = matrix[i][index1];
			matrix[i][index1] = matrix[i][index2];
			matrix[i][index2] = temp;
		}
	}
	
	/**
	 * reflect sudoku grid vertically
	 * @param matrix sudoku matrix
	 */
	public static void Vswap(int [][] matrix){
		for (int i = 0; i < 4; i ++){
			int[] temp = matrix[i];
			matrix[i] = matrix [8-i];
			matrix [8-i] = temp;
		}
	}
	
	/**
	 * reflect sudoku grid vertically
	 * @param matrix sudoku matrix
	 */
	public static void Hswap(int[][] matrix){
		for (int[] row:matrix)
			for (int i = 0; i < 4; i ++){
				int temp = row[i];
				row[i] = row[8-i];
				row[8-i] = temp;
			}
	}
	
	/**
	 * rotate the sudoku clockwise
	 * @param matrix sudoku matrix
	 */
	public static void rotate(int[][] matrix){
		int[][] matrix2 = new int[9][9];

		for (int i = 0; i < 9; i++){
			for (int j = 0;j < 9; j ++){
				matrix2[i][j] = matrix[i][j];
			}
		}
		
		for (int i = 0; i < 9; i++){
			for (int j = 0;j < 9; j ++){
				matrix[i][j] = matrix2[j][8-i];
			}
		}
	}
	
	/**
	 * @return a result of sudoku generated
	 */
	public static int[][] las_vagas(){
		int[][] result= new int[9][9];
		Solver s = new DLXs99();
		for (int i = 0; i < 11; i ++){
			result[gen.nextInt(9)][gen.nextInt(9)] = gen.nextInt(9)+1;
		}
		s.setProblem(result);
		long time = System.currentTimeMillis();
		if (!s.solve()) return null;
		result = s.getMatrix();
		return result;
	}
}
