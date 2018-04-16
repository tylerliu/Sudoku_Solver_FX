package Solvers;

public class OLCol extends OLNode implements Comparable<OLCol>{
	
	public int count = 1;

	public OLCol(short row, OLCol Col) {
		super(row, Col);
	}

	@Override
	public int compareTo(OLCol arg0) {
		return count - arg0.count;
	}

	
}
