package Generator;

import Generator.Difficulty.Level;

public class ProblemGen {

	
	public static int[][] generateProblem(Level level){
		int[][] grid = ResultGen.genResult();
		boolean [][] tried = new boolean[9][9];//define whether 
		Difficulty diff = new Difficulty(level);
		int left = 81;//place tried left
		int row = -1;
		int col = -1;
		int[] dimenion;
		while (left > 0){
			dimenion = diff.nextDig(row,col);
			row = dimenion[0];
			col = dimenion[1];
			if (tried[row-1][col-1]){
				left--;
				continue;
			}
			tried[row-1][col-1] = true; 
			
			if (RestrictionCheck.checkUniqueness(grid,row,col) && diff.canDig(grid, row, col)){
				//System.out.println(row+" "+col);
				//can dig
				grid[row-1][col-1] = 0;
			}
			left --;
		}
		
		ResultGen.transform(grid);
		if (!diff.canFin(grid)){
			System.out.println("No");
			return generateProblem(level);
		}
		return grid;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		long time = System.nanoTime();
		int[][] result = generateProblem(Level.RELAX);
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
	
}
