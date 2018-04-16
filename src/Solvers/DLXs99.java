package Solvers;

/**
 * DLXs99.java solver function for 9 * 9 Sudoku using Dancing Link X
 * 
 * @author TylerLiu
 */
public class DLXs99 implements Solver {

	// Store answer
	private final int maxAnswer = 81 + 3;
	short[] ans = new short[maxAnswer];
	short[] reverseans = new short[maxAnswer];

	// Orthogonal List, (head,c[] is column head) on row 0, n[] normal note
	private final int maxRow = 9 * 9 * 9 + 3;
	// 0 as header, 1-81 as (row,col) have number, 82-162 as row have number 1-9
	// 163-243 row have number 1-9, 244-324 3*3 square have number 1-9
	private final int maxCol = 9 * 9 * 4;
	private final int maxNode = maxRow * 4;
	private int nodeNumber = 0;

	// header node and col node init
	OLCol[] c = new OLCol[maxCol + 1];
	OLNode[] n = new OLNode[maxNode];
	OLNode head;
	int usedAns = -1;// for clean up of the OL, point to last used space

	/**
	 * Constructor--make row nodes
	 */
	public DLXs99() {
		// make constructor of heads, c[]
		head = new OLNode((short) 0, null);
		c[1] = new OLCol((short) 0, null);
		for (int i = 2; i <= maxCol - 1; i++)
			c[i] = new OLCol((short) 0, null);
		c[maxCol] = new OLCol((short) 0, null);

		// make pointer
		head.setlinks(null, null, c[maxCol], c[1]);
		c[1].setlinks(c[1], c[1], head, c[2]);
		for (int i = 2; i <= maxCol - 1; i++)
			c[i].setlinks(c[i], c[i], c[i - 1], c[i + 1]);
		c[maxCol].setlinks(c[maxCol], c[maxCol], c[maxCol - 1], head);
	}

	/**
	 * solve the sudoku
	 * 
	 * @return return if solving is successful
	 */
	public boolean solve() {
		cleanUpList();
		return solve(1);
	}

	/**
	 * solve the sudoku from certain place of sudoku
	 * 
	 * @param attempt
	 */
	private boolean solve(int attempt) { // attempt is (x-1) * 9 + y
		OLNode currentCol = head.getRight();
		if (currentCol == head)
			return true;// problem solved

		OLNode i1 = currentCol;
		while (i1 != head && ((OLCol) currentCol).count > 1) {
			if (((OLCol) i1).count < ((OLCol) currentCol).count)
				currentCol = i1;
			i1 = i1.Right;
		}

		if (((OLCol) currentCol).count < 1)
			return false;

		// start solving-- remove first column
		removeCol(currentCol);

		OLNode i, j;

		i = currentCol.getDown();
		while (i != currentCol) {
			ans[attempt + usedAns] = i.getRow();

			// make small OL for next solve
			j = i.getRight();
			while (j != i) {
				removeCol(j.getCol());
				j = j.getRight();
			}

			if (solve(attempt + 1))
				return true;

			// resume OL
			j = i.getLeft();
			while (j != i) {
				resumeCol(j.getCol());
				j = j.getLeft();
			}

			i = i.getDown();

		}
		resumeCol(currentCol);
		return false;
	}

	/**
	 * solve the sudoku
	 * 
	 * @return return if solving is successful
	 */

	/**
	 * remove column in OL and rows with nodes in that column
	 * 
	 * @param col
	 */
	private void removeCol(OLNode col) {

		// remove read of column
		col.getRight().setLeft(col.getLeft());
		col.getLeft().setRight(col.getRight());

		OLNode i;
		OLNode j;
		i = col.getDown();
		while (i != col) {
			j = i.getRight();
			while (j != i) {
				j.getDown().setUp(j.getUp());
				j.getUp().setDown(j.getDown());
				j.Col.count--;
				j = j.getRight();
			}
			i = i.getDown();
		}
		// System.out.println("removed");
	}

	/**
	 * resume column in OL and rows with nodes in that column
	 * 
	 * @param col
	 */
	private void resumeCol(OLNode col) {

		// resume read of column
		col.getRight().setLeft(col);
		col.getLeft().setRight(col);

		OLNode i;
		OLNode j;
		i = col.getUp();
		while (i != col) {
			j = i.getRight();
			while (j != i) {
				j.getDown().setUp(j);
				j.getUp().setDown(j);
				j.Col.count++;
				j = j.getRight();
			}
			i = i.getUp();
		}
	}

	/**
	 * write in sudoku to line of OL
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @param value
	 */
	public void WriteOV(int rowNumber, int columnNumber, int value) {
		short row = (short) (rowNumber * 100 + columnNumber * 10 + value);
		if (usedAns == -1 || ans[usedAns] % 10 != 0) {
			usedAns++;
			ans[usedAns] = row;
		}
		if (value == 9 && ans[usedAns] % 10 == 0) {
			ans[usedAns] = 0;
			usedAns--;
		}
		if (value == 0) {
			for (int i = 1; i <= 9; i++)
				WriteOV(rowNumber, columnNumber, i);
			return;
		}

		int squareNumber = (rowNumber - 1) / 3 * 3 + (columnNumber - 1) / 3 + 1;

		/*
		 * if Value z in (x,y) of sudoku col1 = (x-1)* 9 +y col2 = (x-1)* 9
		 * +z+81 col3 = (y-1)* 9 +z + 162 col4 = (square - 1)* 9 + z + 243
		 */
		appendLine(row, (rowNumber - 1) * 9 + columnNumber, (rowNumber - 1) * 9 + value + 81,
				(columnNumber - 1) * 9 + value + 162, (squareNumber - 1) * 9 + value + 243);

	}

	/**
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 * @return number of sudoku at certain place
	 */
	public int getAns(int rowNumber, int columnNumber) {
		for (int i = 0; i < ans.length; i++)
			if (ans[i] / 10 == (rowNumber * 10 + columnNumber))
				return ans[i] % 10;
		return 0;
	}

	private void appendLine(short row, int col1, int col2, int col3, int col4) {
		n[nodeNumber + 1] = new OLNode(row, c[col1]);
		n[nodeNumber + 2] = new OLNode(row, c[col2]);
		n[nodeNumber + 3] = new OLNode(row, c[col3]);
		n[nodeNumber + 4] = new OLNode(row, c[col4]);

		// link row
		n[nodeNumber + 1].Right = n[nodeNumber + 2];
		n[nodeNumber + 2].Right = n[nodeNumber + 3];
		n[nodeNumber + 3].Right = n[nodeNumber + 4];
		n[nodeNumber + 4].Right = n[nodeNumber + 1];
		n[nodeNumber + 1].Left = n[nodeNumber + 4];
		n[nodeNumber + 2].Left = n[nodeNumber + 1];
		n[nodeNumber + 3].Left = n[nodeNumber + 2];
		n[nodeNumber + 4].Left = n[nodeNumber + 3];

		// link col
		n[nodeNumber + 1].Down = c[col1];
		n[nodeNumber + 2].Down = c[col2];
		n[nodeNumber + 3].Down = c[col3];
		n[nodeNumber + 4].Down = c[col4];
		n[nodeNumber + 1].Up = c[col1].Up;
		n[nodeNumber + 2].Up = c[col2].Up;
		n[nodeNumber + 3].Up = c[col3].Up;
		n[nodeNumber + 4].Up = c[col4].Up;
		n[nodeNumber + 1].Up.Down = n[nodeNumber + 1];
		n[nodeNumber + 2].Up.Down = n[nodeNumber + 2];
		n[nodeNumber + 3].Up.Down = n[nodeNumber + 3];
		n[nodeNumber + 4].Up.Down = n[nodeNumber + 4];
		n[nodeNumber + 1].Down.Up = n[nodeNumber + 1];
		n[nodeNumber + 2].Down.Up = n[nodeNumber + 2];
		n[nodeNumber + 3].Down.Up = n[nodeNumber + 3];
		n[nodeNumber + 4].Down.Up = n[nodeNumber + 4];

		// increase column Number
		c[col1].count++;
		c[col2].count++;
		c[col3].count++;
		c[col4].count++;

		// update node number
		nodeNumber += 4;
	}

	private void cleanUpList() {
		for (short i : ans) {
			if (i == 0)
				break;
			int x = i / 100;
			int y = i % 100 / 10;
			int z = i % 10;

			// if Value z in (x,y) of sudoku
			int squareNumber = (x - 1) / 3 * 3 + (y - 1) / 3 + 1;
			int col1 = (x - 1) * 9 + y;
			int col2 = (x - 1) * 9 + z + 81;
			int col3 = (y - 1) * 9 + z + 162;
			int col4 = (squareNumber - 1) * 9 + z + 243;

			// delete these column
			removeCol(c[col1]);
			removeCol(c[col2]);
			removeCol(c[col3]);
			removeCol(c[col4]);

		}

	}

	@Override
	public void setProblem(int[][] matrix) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				this.WriteOV(i+1, j+1, matrix[i][j]);
	}

	@Override
	public int[][] getMatrix() {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				result[i][j] = this.getAns(i+1, j+1);
		return result;
	}
	
	public void deletePos(short row){
		int i = 0;
		for (i = 1; i < n.length;i++){
			if (n[i] != null && n[i].getRow() == row)break;
		}
		
		//delete row
		OLNode j = n[i];
		do{
			j.getDown().setUp(j.getUp());
			j.getUp().setDown(j.getDown());
			j.Col.count--;
			j = j.getRight();	
		}while (j != n[i]);
	}
}
