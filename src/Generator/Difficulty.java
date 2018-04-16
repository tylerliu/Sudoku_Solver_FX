package Generator;

import java.util.Random;

public class Difficulty {

	/**
	 * Represent a difficulty of the puzzle
	 * Using a point system to define whether the difficulty
	 * 										score: 	1		2			3			4			5
	 *  1. amount of cell originally in the system: >50		39-49		31-35		27-30		21-26
	 *  2. lowest filled for rows/columns/blocks:	5		4			3			2			0  
	 *  3. highest filled for rows/columns/blocks:  9		8			6-7			4-5			3
	 *  3. Product of all possible combination to fill: <1E2, 1E2-1E3, 1E3-1E4		1E4-1E5		>1E5
	 * 	
	 * @author Tyler Liu
	 *
	 */
	public enum Level{RELAX, EASY, NORMAL, HARD, INSANE}
	Level level;
	public Difficulty(Level level){
		this.level = level;
	}
	
	public boolean canDig(int[][] matrix, int row, int col){
		
		row--;
		col--;
		int count = 0;
		for(int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j ++)
				if (matrix[i][j] != 0) count ++;
		switch (level){
		case RELAX:
			if (count <= 50) return false;
			break;
		case EASY:
			if (count <= 39) return false;
			break;
		case NORMAL: 
			if (count <= 31) return false;
			break;
		case HARD:
			if (count <= 27) return false;
			break;
		case INSANE:
			if (count <= 19) return false;
			break;
		}
		
		//count lowest filled in row/col
		int rowCount = 0; 
		count = 0;
			for (int i = 0; i < 9; i++) {
				if (matrix[i][col] != 0)
					count ++;
				if (matrix[row][i] != 0)
					rowCount ++;
			}
		rowCount = Integer.min(count, rowCount);
		count = 0;
			
			for (int i = row / 3 * 3; i < row / 3 * 3 + 3; i++)
				for (int j = col / 3 * 3; j < col / 3 * 3 + 3; j++)
					if (matrix[i][j] != 0)
						count ++;
		count = Integer.min(count, rowCount);//count have highest
		
		switch (level){
		case RELAX:
			if (count <= 5) return false;
			break;
		case EASY:
			if (count <= 4) return false;
			break;
		case NORMAL: 
			if (count <= 3) return false;
			break;
		case HARD:
			if (count <= 2) return false;
			break;
		case INSANE:
			if (count <= 0) return false;
			break;
		}
	
		return true;
	}
	
	public boolean canFin(int[][] matrix){
		int score = 0;
		int count = 0;
		for(int i = 0; i < 9; i ++)
			for (int j = 0; j < 9; j ++)
				if (matrix[i][j] != 0) count ++;
		
		if (count >= 50)
			score += 1;
		if (count >= 37 && count <= 49)
			score += 2;
		if (count >= 31 && count <= 37)
			score += 3;
		if (count >= 27 && count <= 30)
			score += 4;
		if (count >= 21 && count <= 26)
			score += 5;

		
		//count lowest filled in row/col
		int maxCount = Integer.MIN_VALUE;
		int minCount = Integer.MAX_VALUE;
		int colCount = 0;
		int rowCount = 0; 
		for (int k = 0; k < 9; k++){
			for (int i = 0; i < 9; i++) {
				if (matrix[i][k] != 0)
					colCount ++;
				if (matrix[k][i] != 0)
					rowCount ++;
			}
			
			maxCount = Integer.max(maxCount, colCount);
			maxCount = Integer.max(maxCount, rowCount);
			minCount = Integer.min(minCount, colCount);
			minCount = Integer.min(minCount, rowCount);
		rowCount = 0;
		colCount = 0;
		}
		
		for (int k = 0; k < 3; k++)
			for (int l = 0; l < 3; l++){
				for (int i = k * 3; i < k * 3 + 3; i++)
					for (int j = l * 3; j < l * 3 + 3; j++)
						if (matrix[i][j] != 0)
							rowCount ++;
				maxCount = Integer.max(maxCount, rowCount);
				minCount = Integer.min(minCount, rowCount);
				rowCount = 0;
			}
			
		//System.out.println(minCount);
		//System.out.println(maxCount);
			if (minCount >= 5)score+= 1;
				if(maxCount >= 9) score+= 1;
			if (minCount == 4) score+= 2;
				if( maxCount == 8) score+= 2;
			if (minCount == 3)score+= 3;
				if(maxCount == 7 || maxCount == 6) score+= 3;
			if (minCount == 2) score+= 4;
				if(maxCount == 5 || maxCount == 4) score+= 4;
			if (minCount <= 1) score+= 5;
				if(maxCount <= 3) score+= 5;
		
		boolean[][] check = new boolean[27][9];
		
		//row
		for(int i = 0; i < 9; i ++){
			for (int j = 0;j < 9; j ++){
				if(matrix[i][j]!= 0){
					check[i][matrix[i][j]-1] = true;
					check[j+9][matrix[i][j]-1] = true;
				}
			}
		}
		
		//block
		for (int k = 0; k < 3; k++)
			for (int l = 0; l < 3; l++){
				for (int i = k * 3; i < k * 3 + 3; i++)
					for (int j = l * 3; j < l * 3 + 3; j++)
						if(matrix[i][j]!= 0)
							check[k*3+l+18][matrix[i][j]-1] = true;
			}
		
		double product = 0;
		for(int i = 0; i < 9; i ++){
			for (int j = 0;j < 9; j ++){
				if(matrix[i][j] == 0){
					count = 0;
					//count the false
					for (int k = 0; k < 9;k++){
						if (!(check[i][k] && check[j+8][k] && check[i/3*3+j/3][k]))count++;
					}
					if (count == 0)System.out.println(i+" "+j);
					product = product + Math.log10(count);
				}
			}
		}
		
		//System.out.println(product+" "+score);
			if (product <= 30) score+= 1;
			if (product >= 30 && product <= 38) score+= 2;
			if (product >= 38 && product <= 48) score+= 3;
			if (product >= 48 && product <= 55) score+= 4;
			if (product >= 55) score+= 5;
	
		switch (level){
		case RELAX:
			return score>=2 && score<= 6;
		case EASY:
			return score>=6 && score<= 10;
		case NORMAL: 
			return score>=10 && score<= 14;
		case HARD:
			return score>=14 && score<= 18;
		case INSANE:
			return score>=18 && score<= 22;
		}
		return false;
	}
	
	/**
	 * return the position of next digging
	 * HAVE TO CHECK WHETHER NEXT DIG IS DIGGED BEFORE!!!!
	 * if this dig is not valid, call this with last falled dig value
	 * @param dimenion the position of last dig, define by row, col, 1-9;
	 * @return the place of next dig, defined by row, col, 1-9;
	 */
	public int[] nextDig(int... dimenion){
		if (dimenion[0] == -1 || dimenion[1] == -1)return new int[]{1,1};
		dimenion[0]--;
		dimenion[1]--;
		switch (level){
		case RELAX:
		case EASY:
			//randomizing
			Random r = new Random();
			return new int[]{r.nextInt(9)+1,r.nextInt(9)+1};
		case NORMAL: 
			if (dimenion[0] == 8 && dimenion[1] == 8)return new int[]{1,2};
			if (dimenion[0] == 8 && dimenion[1] == 7)return new int[]{1,1};
			
			if (dimenion[0] % 2 == 0)dimenion[1] = 8-dimenion[1];//reverse
			int next = dimenion[0]*9+dimenion[1]+2;
			dimenion[0] = next/9;
			dimenion[1] = next%9;
			if (dimenion[0] % 2 == 0)dimenion[1] = 8-dimenion[1];//reverse back
			dimenion[0]++;
			dimenion[1]++;
			return dimenion;
		case HARD:
		case INSANE:
			if (dimenion[0] == 8 && dimenion[1] == 8)return new int[]{1,1};
			int i = dimenion[0]*9+dimenion[1]+1;
			dimenion[0] = i/9+1;
			dimenion[1] = i%9+1;
			return dimenion;
		}
		return null;
	}
	
}
