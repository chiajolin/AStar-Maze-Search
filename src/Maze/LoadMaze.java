package Maze;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.border.LineBorder;

public class LoadMaze extends JPanel {
	private int mazeDimension = GenMaze.mazeDimension;
	private JTable table;
	private Cell cell[][] = new Cell[mazeDimension][mazeDimension];
	private int StartX;
	private int StartY;
	private int TargetX;
	private int TargetY;
	private int MazeNumber;

	/**
	 * Create the panel.
	 * 
	 * @throws Exception
	 */
	public Cell[][] getCellArray() {
		return cell;
	}

	public LoadMaze(int mazeNumber) throws Exception {
		MazeNumber = mazeNumber;
		LoadCell(MazeNumber);
		mazeLayout();
	}

	private void mazeLayout() {
		setLayout(new GridLayout(0, 1, 0, 0));

		DefaultTableModel model = new DefaultTableModel(mazeDimension, mazeDimension);
		table = new JTable(){
            //table cell tool tips.           
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int row = rowAtPoint(p);
                int column = columnAtPoint(p);
                try {
                	tip = "g(s):" +  cell[row][column].G + ",h(s):" + cell[row][column].H;
                } catch (RuntimeException e1) {//if mouse is over an empty line
                }

                return tip;
            }
        };
		table.setForeground(Color.RED);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setModel(model);
		table.setGridColor(Color.BLACK);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < mazeDimension; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(1);
		}
		table.setDefaultRenderer(Object.class, new RenderColor(cell));
		table.setTableHeader(null);

		// ScrollBar
		JScrollPane tableScrollPane = new JScrollPane();
		add(tableScrollPane);
		tableScrollPane.setViewportView(table);

		table.setValueAt(cell[StartY][StartX].getCellType(), StartY, StartX);
		table.setValueAt(cell[TargetY][TargetX].getCellType(), TargetY, TargetX);
	}

	public void setCellType(String cellType, int y, int x) {
		table.setValueAt(cellType, y, x);
		cell[y][x].setCellType(cellType);
	}

	public String getCellType(int y, int x) {
		return cell[y][x].getCellType();
	}

	private void LoadCell(int mazeNumber) throws Exception {
		SAXReader reader = new SAXReader();
		File file = new File("./file/" + mazeNumber + ".xml");
		Document document = reader.read(file);
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element child : childElements) {
			if (child.getName().equals("cell")) {
				int x = Integer.parseInt(child.elementText("X"));
				int y = Integer.parseInt(child.elementText("Y"));
				int s = Integer.parseInt(child.elementText("S"));
				String CellType = child.elementText("CellType");
				cell[y][x] = new Cell(y, x);
				cell[y][x].setS(s);
				cell[y][x].setCellType(CellType);
			}
			if (child.getName().equals("StartTarget")) {
				StartX = Integer.parseInt(child.elementText("StartX"));
				StartY = Integer.parseInt(child.elementText("StartY"));
				TargetX = Integer.parseInt(child.elementText("TargetX"));
				TargetY = Integer.parseInt(child.elementText("TargetY"));
			}

		}
		// reveal cells of the agent and around the agent, so that the algorithms work correctly
		cell[StartY][StartX].setKnownS(cell[StartY][StartX].getS());
		if (StartY > 0) {
				cell[StartY - 1][StartX].setKnownS(cell[StartY - 1][StartX].getS());
		}
		if (StartY < mazeDimension - 1) {
			// south neighbor
				cell[StartY + 1][StartX].setKnownS(cell[StartY + 1][StartX].getS());
		}
		if (StartX > 0) {
			// left neighbor
				cell[StartY][StartX - 1].setKnownS(cell[StartY][StartX - 1].getS());
		}
		if (StartX < mazeDimension - 1) {
			// right neighbor
				cell[StartY][StartX + 1].setKnownS(cell[StartY][StartX + 1].getS());
		}
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
}

final class RenderColor extends DefaultTableCellRenderer {
	private Cell cell[][];

	public RenderColor(Cell cell[][]) {
		this.cell = cell;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object numberValue, boolean isSelected,
			boolean hasFocus, int row, int column) {
		Component renderer = super.getTableCellRendererComponent(table, numberValue, isSelected, hasFocus, row, column);
		switch (cell[row][column].getKnownS()) {
		case 0:
			renderer.setBackground(Color.gray); // unvisited
			break;
		case 1:
			renderer.setBackground(Color.white); // unblocked
			break;
		case 2:
			renderer.setBackground(Color.black); // blocked
			break;
		}
		setValue(cell[row][column].getCellType());
		return this;
	}
}
