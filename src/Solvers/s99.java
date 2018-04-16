package Solvers;

/**
 * s99.java solver function for 9 * 9 sudoku
 * 
 * @author TylerLiu
 * 
 */
public class s99 implements Solver {
	private int[] value = new int[11];// row number as suffix only
	private int[] reverseValue = new int[11];// row number as suffix only

	/**
	 * check if condition of sudoku work around a number
	 * 
	 * @param rowNumber
	 *            columnNumber
	 */
	private boolean CheckSpace(int rowNumber, int columnNumber) {
		int ok, i, j;
		ok = 1;
		i = 0;
		j = 0;
		if (getvalue(rowNumber, columnNumber) != 0) {
			for (i = 1; i <= 9; i++) {
				if (i != rowNumber && getvalue(rowNumber, columnNumber) == getvalue(i, columnNumber))
					ok = 0;
				if (i != columnNumber && getvalue(rowNumber, columnNumber) == getvalue(rowNumber, i))
					ok = 0;
			}
			i = 0;
			for (i = (rowNumber - 1) / 3 * 3 + 1; i <= (rowNumber - 1) / 3 * 3 + 3 && i < 9; i++)
				for (j = (columnNumber - 1) / 3 * 3 + 1; j <= (columnNumber - 1) / 3 * 3 + 3 && i < 9; j++)
					if (i != rowNumber && j != columnNumber && getvalue(rowNumber, columnNumber) == getvalue(i, j))
						ok = 0;
		}
		if (ok == 0)
			return false;
		else
			return true;
	}

	/**
	 * solve the sudoku
	 * 
	 * @return return if solving is successful
	 */
	public boolean solve() {
		return solve(1, 1);
	}

	/**
	 * solve the sudoku from certain place of sudoku
	 * 
	 * @param rowNumber
	 *            columnNumber
	 */
	private boolean solve(int rowNumber, int columnNumber) {
		// System.out.println(rowNumber+" " + columnNumber);
		int i;
		if (rowNumber >= 10) {
			return true;
		}
		if (!(CheckSpace(rowNumber, columnNumber))) {
			return false;
		}
		if (getvalue(rowNumber, columnNumber) != 0) {
			if (CheckSpace(rowNumber, columnNumber)) // if given in input
			{
				if (columnNumber >= 9)
					return solve(rowNumber + 1, 1);
				else
					return solve(rowNumber, columnNumber + 1);
			} else
				return false;
		}

		// if did not put in value
		for (i = 1; i <= 9; i++) {
			setvalue(rowNumber, columnNumber, i);
			if (CheckSpace(rowNumber, columnNumber)) {
				if (columnNumber >= 9)
					if (solve(rowNumber + 1, 1))
						return true;
				if (columnNumber <= 8 && solve(rowNumber, columnNumber + 1))
					return true;
			}

		}
		setvalue(rowNumber, columnNumber, 0);
		// System.out.printf("%d%d\n",rowNumber,columnNumber);
		return false;

	}

	/**
	 * solve the sudoku
	 * 
	 * @return return if solving is successful
	 */
	private boolean reversesolve() {
		return reversesolve(1, 1);
	}

	/**
	 * solve the sudoku from certain place of sudoku in reverse direction
	 * 
	 * @param rowNumber
	 *            start place of solving row number
	 * @param columnNumber
	 *            start place of solving row number
	 */
	private boolean reversesolve(int rowNumber, int columnNumber) {
		// System.out.println(rowNumber+" " + columnNumber);
		int i;
		if (rowNumber >= 10) {
			return true;
		}
		if (!(CheckSpace(rowNumber, columnNumber))) {
			return false;
		}
		if (getreversevalue(rowNumber, columnNumber) != 0) {
			if (CheckSpace(rowNumber, columnNumber)) // if given in output
			{
				if (columnNumber >= 9)
					return reversesolve(rowNumber + 1, 1);
				else
					return reversesolve(rowNumber, columnNumber + 1);
			} else
				return false;
		}

		// if did not put in value
		for (i = 9; i >= 1; i--) {
			setreversevalue(rowNumber, columnNumber, i);
			if (CheckSpace(rowNumber, columnNumber)) {
				if (columnNumber >= 9)
					if (reversesolve(rowNumber + 1, 1))
						return true;
				if (columnNumber <= 8 && reversesolve(rowNumber, columnNumber + 1))
					return true;
			}

		}
		setreversevalue(rowNumber, columnNumber, 0);
		// System.out.printf("%d%d\n",rowNumber,columnNumber);
		return false;

	}

	/**
	 * write in both input number of the sudoku
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @param value
	 */
	public void WriteOV(int rowNumber, int columnNumber, int value) {
		if (columnNumber == 1) {
			reverseValue[rowNumber] = 0;
			this.value[rowNumber] = 0;
		}

		setreversevalue(rowNumber, columnNumber, value);
		setvalue(rowNumber, columnNumber, value);
	}

	/**
	 * return value of a place in sudoku stored in cvalue[]
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @return value
	 */
	private int getvalue(int rowNumber, int columnNumber) {
		if (rowNumber < 1 || rowNumber > 9 || columnNumber < 1 || columnNumber > 9)
			return 0;
		return value[rowNumber] % powerof10(columnNumber) / powerof10(columnNumber - 1);
	}

	/**
	 * write value of a place in sudoku stored in value[]
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @param value
	 */
	private void setvalue(int rowNumber, int columnNumber, int value) {
		if (rowNumber < 1 || rowNumber > 9 || columnNumber < 1 || columnNumber > 9 || value < 0 || value > 9)
			return;
		this.value[rowNumber] = this.value[rowNumber]
				+ (value - getvalue(rowNumber, columnNumber)) * powerof10(columnNumber - 1);

	}

	public int getAns(int rowNumber, int columnNumber) {
		return getvalue(rowNumber, columnNumber);
	}

	/**
	 * return value of a place in sudoku stored in reversevalue[]
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @return value
	 */
	private int getreversevalue(int rowNumber, int columnNumber) {
		if (rowNumber < 1 || rowNumber > 9 || columnNumber < 1 || columnNumber > 9)
			return 0;
		return reverseValue[rowNumber] % powerof10(columnNumber) / powerof10(columnNumber - 1);
	}

	/**
	 * write value of a place in sudoku stored in reversevalue[]
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @param value
	 */
	private void setreversevalue(int rowNumber, int columnNumber, int value) {
		if (rowNumber < 1 || rowNumber > 9 || columnNumber < 1 || columnNumber > 9 || value < 0 || value > 9)
			return;
		reverseValue[rowNumber] = reverseValue[rowNumber]
				+ (value - getreversevalue(rowNumber, columnNumber)) * powerof10(columnNumber - 1);
	}

	/**
	 * check if Sudoku have only one solution
	 */
	public boolean onlyOneSolution() {
		return (solve() && reversesolve() && value == reverseValue);
	}

	/**
	 * calculate 10^n power
	 * 
	 * @param exponent
	 * @return 10^exponent
	 */
	public int powerof10(int exponent) {
		int power = 1;
		for (int i = 1; i <= exponent; i++) {
			power *= 10;
		}
		return power;
	}

	@Override
	public void setProblem(int[][] matrix) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				this.WriteOV(i + 1, j + 1, matrix[i][j]);
	}

	@Override
	public int[][] getMatrix() {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				result[i][j] = this.getAns(i + 1, j + 1);
		return result;
	}

}
