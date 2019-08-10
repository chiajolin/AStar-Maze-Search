package Maze;

public class Cell {
	private int X; // x coordinate
	private int Y; // y coordinate
	private int S; // maze cell state : 0 for unvisited, 1 for unblocked, 2 for
					// blocked
	private int KnownS;// known state: 0 for unvisited, 1 for unblocked, 2 for
						// blocked
	private String CellType; // the string show on the maze cell after A* : A
								// for start, T for target, 0 for open cell, 1
								// for end cell, * for the A* route
	public int G; // g(s)
	public int H; // h(s)
	private int Search; // last touched at search number #
	public Cell Tree; // tree pointer - points to best predecessor

	public Cell(int y, int x) {
		X = x;
		Y = y;
		S = 0;
		KnownS=0;
		CellType = " ";
	}

	public int getS() {
		return S;
	}

	public void setS(int s) {
		S = s;
	}
	public int getKnownS() {
		return KnownS;
	}

	public void setKnownS(int s) {
		KnownS = s;
	}

	public int getSearch() {
		return Search;
	}

	public void setSearch(int s) {
		Search = s;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public String getCellType() {
		return CellType;
	}

	public void setCellType(String cellType) {
		CellType = cellType;
	}

	public int ManhattanDistance(Cell other) {
		return Math.abs(Y - other.getY()) + Math.abs(X - other.getX());
	}

	public int CostTo(Cell other) {
		if (ManhattanDistance(other) > 1) {
			throw new RuntimeException("attempt to get cost to non-adjacent cell!");
		} else if ((KnownS !=2) && (other.getKnownS() !=2)) {
			return 1;
		} else {
			return Integer.MAX_VALUE;
		}
	}

}