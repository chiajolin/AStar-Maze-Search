package Maze;

import java.util.Random;
import java.util.Stack;

public class GenMaze{
	public static int mazeDimension = 101;
	private Cell cell[][] = new Cell[mazeDimension][mazeDimension];
	private int StartX;
	private int StartY;
	private int TargetX;
	private int TargetY;
	private int unvisitedCounter;

	/**
	 * Create the panel.
	 */
	public GenMaze() {
		generateCell();
		dfsGenerate();
		genStartTargetCell();
		setStartTarget();
	}

	private void setStartTarget() {
		cell[StartY][StartX].setCellType(cell[StartY][StartX].getCellType());
		cell[TargetY][TargetX].setCellType(cell[TargetY][TargetX].getCellType());
	}

	private void generateCell() {
		for (int i = 0; i < mazeDimension; i++) {
			for (int j = 0; j < mazeDimension; j++) {
				cell[i][j] = new Cell(i, j);
			}
		}
	}

	private int randomNumber(int size) {
		Random generator = new Random();
		int randomNumber = generator.nextInt(size);
		return randomNumber;
	}

	// generate Start and Target Cell
	private void genStartTargetCell() {
		StartX = randomNumber(mazeDimension);
		StartY = randomNumber(mazeDimension);

		while (cell[StartY][StartX].getS() == 2) {// 2 is blocked
			StartX = randomNumber(mazeDimension);
			StartY = randomNumber(mazeDimension);
		}

		cell[StartY][StartX].setCellType("A");

		TargetX = randomNumber(mazeDimension);
		TargetY = randomNumber(mazeDimension);

		while (TargetX == StartX && TargetY == StartY || cell[TargetY][TargetX].getS() == 2) {
			TargetX = randomNumber(mazeDimension);
			TargetY = randomNumber(mazeDimension);
		}

		cell[TargetY][TargetX].setCellType("T");
	}

	public Cell getCell(int y, int x) {
		return cell[y][x];
	}
	
	// get Start Cell
	public Cell getStartCell() {
		return cell[StartY][StartX];
	}

	// get Target Cell
	public Cell getTargetCell() {
		return cell[TargetY][TargetX];
	}

	private void dfsGenerate() {
		// start from a random cell
		int x;
		int y;
		unvisitedCounter = mazeDimension * mazeDimension;
		while (unvisitedCounter > 0) {
			x = randomNumber(mazeDimension); // x : column
			y = randomNumber(mazeDimension); // y : row
			while (cell[y][x].getS() != 0) { // visited cell
				if (x + 1 <= mazeDimension - 1) {
					x++;
				} else {
					x = 0;
					if (y + 1 <= mazeDimension - 1) {
						y++;
					} else {
						y = 0;
					}
				}
				// checks each cell to see if it's unvisited, starting from a
				// random cell and going in a cycle
				// there must be an unvisited cell in this loop
			}
			dfsGenerate(x, y);
		}

	}

	private void dfsGenerate(int x, int y) {
		Stack<Cell> st = new Stack<Cell>();
		cell[y][x].setS(1);
		unvisitedCounter--;
		// select a random neighbouring cell and do dfs search to generate maze
		while ((y >= 1 && cell[y - 1][x].getS() == 0) || (y + 1 <= mazeDimension - 1 && cell[y + 1][x].getS() == 0)
				|| (x + 1 <= mazeDimension - 1 && cell[y][x + 1].getS() == 0)
				|| (x >= 1 && cell[y][x - 1].getS() == 0)) {
			int direction = randomNumber(4); // 0 for north, 1 for south, 2
												// for right, 3 for left

			boolean neighborfound = false;
			while (neighborfound == false) {
				// start from a random direction, try to find next unvisited
				// direction in a cycle, since there must be one unvisited
				if (direction == 0) {
					if (y >= 1 && cell[y - 1][x].getS() == 0) {
						if (randomNumber(100) <= 29) { // 30% blocked
							cell[y - 1][x].setS(2);
						} else {
							cell[y - 1][x].setS(1);
							st.push(cell[y][x]);
							y = y - 1;
						}
						neighborfound = true;
					} else {
						direction++;
					}
				}
				if (direction == 1) {
					if (y + 1 <= mazeDimension - 1 && cell[y + 1][x].getS() == 0) {
						if (randomNumber(100) <= 29) { // 30% blocked
							cell[y + 1][x].setS(2);
						} else {
							cell[y + 1][x].setS(1);
							st.push(cell[y][x]);
							y = y + 1;
						}
						neighborfound = true;
					} else {
						direction++;
					}
				}

				if (direction == 2) {
					if (x + 1 <= mazeDimension - 1 && cell[y][x + 1].getS() == 0) {
						if (randomNumber(100) <= 29) { // 30% blocked
							cell[y][x + 1].setS(2);
						} else {
							cell[y][x + 1].setS(1);
							st.push(cell[y][x]);
							x = x + 1;
						}
						neighborfound = true;
					} else {
						direction++;
					}
				}

				if (direction == 3) {
					if (x >= 1 && cell[y][x - 1].getS() == 0) {
						if (randomNumber(100) <= 29) { // 30% blocked
							cell[y][x - 1].setS(2);
						} else {
							cell[y][x - 1].setS(1);
							st.push(cell[y][x]);
							x = x - 1;
						}
						neighborfound = true;
					} else {
						direction = 0;// wrap back to 0 and test in next
										// while loop
					}
				}

			}
			// unvisited neighbor processed, position updated
			unvisitedCounter--;
		}
		if (!st.empty()) {
			// dead-end pop
			Cell popCell = st.pop();
			x = popCell.getX();
			y = popCell.getY();
		}
		// one dfs pass finished
	}
}

