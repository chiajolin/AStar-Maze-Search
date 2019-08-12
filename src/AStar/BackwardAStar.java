package AStar;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;
import Maze.Cell;
import Maze.Frame;

public class BackwardAStar implements Comparator<Cell> {
	private Cell startCell = Frame.mazeList.getCurrentMaze().getStartCell();
	private Cell targetCell = Frame.mazeList.getCurrentMaze().getTargetCell();
	private PriorityQueue<Cell> openList = new PriorityQueue<Cell>(11, this);

	Cell cell[][] = Frame.mazeList.getCurrentMaze().getCellArray();
	int counter;
	int stepcount=0; //numbers of steps walked by agent
	int blockcount=0; //number of times the path is blocked
	int expandcount=0; //number of cells expanded(put on closed list)
	int checkcount=0; //number of cell cost checks(put on open list)

	public int F(Cell x) {
		// because h is handled differently in different methods, need different
		// F
		if (x.G == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} // guard against overflow
		return x.ManhattanDistance(startCell) + x.G;
	}

	public BackwardAStar() {
		int i;int j;Stack<Cell> st= new Stack<Cell>();
		for(i=0;i<cell.length;i++)
		{
			for(j=0;j<cell.length;j++) //because it's square
			{
				cell[i][j].setSearch(0);
			}
		}
		counter = 0; // xth execution of ComputePath
		while (startCell!= targetCell) {
			counter++;
			targetCell.G=0;
			targetCell.setSearch(counter);
			startCell.G=Integer.MAX_VALUE;
			startCell.setSearch(counter);
			if(ComputePath()==1)
			{
				Frame.lblShowData.setText("I cannot reach the target. steps:"+stepcount+", blocks:"+blockcount+" expanded:"+expandcount+", checked:"+checkcount+".");

				return;
			}
			 st.clear();
			 
			while(startCell!=targetCell)
			{
				
				//if block on path is found, stop and recompute
				//(the block if it exists should have been known at this point because we just came from its neighbor)
				if(startCell.Tree.getKnownS()==2){blockcount++;break;}
				if(startCell.Tree!=targetCell){startCell.Tree.setCellType("*");}stepcount++;
				startCell=startCell.Tree;
				//check unknown neighbors and update known states
				
				if (startCell.getY() > 0) {
					// north neighbor
					if(cell[startCell.getY() - 1][startCell.getX()].getKnownS()==0)
					{
						cell[startCell.getY() - 1][startCell.getX()].setKnownS(cell[startCell.getY() - 1][startCell.getX()].getS());
					}
				}
				if (startCell.getY() < cell.length-1) {
					// south neighbor
					if(cell[startCell.getY() + 1][startCell.getX()].getKnownS()==0)
					{
						cell[startCell.getY() + 1][startCell.getX()].setKnownS(cell[startCell.getY() + 1][startCell.getX()].getS());
					}
				}
				if (startCell.getX() > 0) {
					// left neighbor
					if(cell[startCell.getY()][startCell.getX()-1].getKnownS()==0)
					{
						cell[startCell.getY()][startCell.getX() - 1].setKnownS(cell[startCell.getY()][startCell.getX() - 1].getS());
					}
				}
				if (startCell.getX()<cell.length-1) {
					// right neighbor
					if(cell[startCell.getY()][startCell.getX()+1].getKnownS()==0)
					{
						cell[startCell.getY()][startCell.getX()+1].setKnownS(cell[startCell.getY()][startCell.getX()+1].getS());
					}
				}
			}
			Frame.mazeList.getCurrentMaze().repaint();
		}
		Frame.lblShowData.setText("target reached. steps:"+stepcount+", blocks:"+blockcount+" expanded:"+expandcount+", checked:"+checkcount+".");

	}

	public int ComputePath() {
		openList.clear();
		openList.add(targetCell);
		Cell current;
		Cell temp;
		while (openList.isEmpty() == false) {
			current = openList.remove();expandcount++;
			if(current.getCellType().equals(" ")){current.setCellType(Integer.toString(counter%10));}
			if (F(current) >= startCell.G) {
				return 0;// best known path found
			}
			if ((current.getY() > 0)
					&& (current.CostTo(cell[current.getY() - 1][current.getX()]) < Integer.MAX_VALUE)) {
				// north neighbor accessible (must check to prevent cost
				// overflow)
				temp = cell[current.getY() - 1][current.getX()];
				processNeighbor(current, temp);
			}
			if ((current.getY() < cell.length - 1)
					&& (current.CostTo(cell[current.getY() + 1][current.getX()]) < Integer.MAX_VALUE)) {
				// south neighbor accessible (must check to prevent cost
				// overflow)
				temp = cell[current.getY() + 1][current.getX()];
				processNeighbor(current, temp);
			}
			if ((current.getX() > 0)
					&& (current.CostTo(cell[current.getY()][current.getX() - 1]) < Integer.MAX_VALUE)) {
				// left neighbor accessible (must check to prevent cost
				// overflow)
				temp = cell[current.getY()][current.getX() - 1];
				processNeighbor(current, temp);
			}
			if ((current.getX() < cell.length - 1)
					&& (current.CostTo(cell[current.getY()][current.getX() + 1]) < Integer.MAX_VALUE)) {
				// right neighbor accessible (must check to prevent cost
				// overflow)
				temp = cell[current.getY()][current.getX() + 1];
				processNeighbor(current, temp);
			}

		}
		return 1;// no path
	}

	public void processNeighbor(Cell current, Cell neighbor) {
		checkcount++;
		if (neighbor.getSearch() < counter) {
			neighbor.G = Integer.MAX_VALUE;
			neighbor.setSearch(counter);
		}
		if (neighbor.G > current.G + current.CostTo(neighbor)) {
			neighbor.G = current.G + current.CostTo(neighbor);
			neighbor.Tree = current;
			openList.remove(neighbor);// remove if it is already there
			openList.add(neighbor);
		}
	}

	@Override
	public int compare(Cell a, Cell b) {
		if (F(a) == F(b)) {
			if (a.G == b.G) {
				if (a.getY() == b.getY()) {//favor smaller Y (up) and smaller X(left)
					return a.getX() - b.getX();
				} else {
					return a.getY() - b.getY();
				}
			} else {
				return b.G - a.G;
			} // prioritize larger G if F are equal
		} else {
			return F(a) - F(b);
		} // prioritize smaller F

	}

}