package Solvers;

public class OLNode {
	public OLNode Left;
	public OLNode Right;
	public OLNode Up;
	public OLNode Down;
	public OLCol Col;
	public short row;
	
	/**
	 * Constructor
	 * @param row
	 * @param Col
	 */
	public OLNode(short row, OLCol Col){
		this(row,Col,null,null,null,null);
	}
	
	/**
	 * Constructor
	 * @param row
	 * @param Col
	 * @param Up
	 * @param Down
	 * @param Left
	 * @param Right
	 */
	public OLNode(short row, OLCol Col,OLNode Up ,OLNode Down ,OLNode Left ,OLNode Right){
		super();
		this.row = row;
		this.Col = Col;
		this.Up = Up;
		this.Down = Down;
		this.Left = Left;
		this.Right = Right;
	}
	
	public OLNode getRight() {  
		return this.Right;  
	}  
	  
	public void setRight(OLNode Right) {  
		this.Right = Right;  
	}  
	  
	public OLNode getDown() {  
		return this.Down;  
	}  
	  
	public void setDown(OLNode Down) {  
		this.Down = Down;  
	} 
	
		public OLNode getUp() {  
		return this.Up;  
	}  
	  
	public void setUp(OLNode Up) {  
		this.Up = Up;  
	}  
	
	public OLNode getLeft() {  
		return this.Left;  
	}  
	  
	public void setLeft(OLNode Left) {  
		this.Left = Left;  
	}  
	

	
	public OLNode getCol() {  
		return this.Col;  
	}  
	  
	public void setCol(OLCol Col) {  
		this.Col = Col;  
	}  
	
	public short getRow(){
		return this.row;
	}
	
	public void setRow(short row){
		this.row = row;
	}
	
	public void setlinks(OLNode up, OLNode down, OLNode left, OLNode right){
		Up = up;
		Down = down;
		Left = left;
		Right = right;
	}
	
}
