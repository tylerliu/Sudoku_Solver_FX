package Generator;

import java.util.Scanner;

import Solvers.DLXs99;
import Solvers.Solver;
import Solvers.s99;

public class RestrictionCheck {

	/**
	 * 
	 * @return a boolean tells whether the array is unique after dig the point
	 * @param array the sudoku array
	 * @param row row index from 1-9 of array of index to be remove
	 * @param col column index from 1-9 of array of index to be remove
	 * @precondition the cell that selected need to fill with  number, or it is not equal to 0
	 */
	public static boolean checkUniqueness(int[][] array, int row, int col){
		
		int val = array[row-1][col-1];
		assert val == 0;
		array[row-1][col-1] = 0;
		DLXs99 solver = new DLXs99();
		solver.setProblem(array);
		solver.deletePos((short) (row*100+col*10+val));
		
		array[row-1][col-1] = val;
		return !solver.solve();
	}
	
	public static void main(String[] args){
		int[][] array = new int[9][9];
		Scanner scan = new Scanner(System.in);
	    System.out.println("Please Input Sudoku:");
	    int i,j;
	    String str = "";
	    for(i=1;i<=9;i++)//ÐÐ
	        for(j=1;j<=9;j++)//ÁÐ
	        {
	                if ( str.length() == 0 )  str= scan.nextLine();
	                
	                if( str.length() != 0 && str.charAt(0)<='9' && str.charAt(0)>='0' ){
	                    array[i-1][j-1] = (int) str.charAt(0) - 48;
	                }
	                else j--;
	                str = str.substring(1);
	        }
	    
	    scan.close();
	    System.out.println(checkUniqueness(array,2,1));
	}
}
