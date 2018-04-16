package Solvers;

public interface Solver {
	
	/**
	 * read in the value of the cell in the puzzle
	 * value should be 0 <= value <= 9, does not count if value == 0
	 * @param rowNumber
	 * @param columnNumber
	 * @param value
	 * 
	 */
	public void WriteOV(int rowNumber,int columnNumber, int value);
	
	/**
	 * Solve the puzzle base on the input
	 * @return true if the puzzle solved
	 */
	public boolean solve();
	
	/**
	 * output the answer of the cell in the puzzle
	 * DO NOT CALL IF SOLVE NOT CALLED OR RETURN FALSE!!
	 * @param rowNumber
	 * @param columnNumber
	 * @return the answer to the cell
	 */
	public int getAns(int rowNumber, int columnNumber);
	
	/**
	 * sets the problem with the matrix
	 * @param matrix the sudoku problem
	 */
	public void setProblem(int[][] matrix);
	
	/**
	 * return the matrix result of the problem
	 * DO NOT CALL IF SOLVE NOT CALLED OR RETURN FALSE!!
	 * @return matrix of the result
	 */
	public int[][] getMatrix();
}
