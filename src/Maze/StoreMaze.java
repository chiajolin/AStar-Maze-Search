package Maze;

import java.io.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class StoreMaze {
	public StoreMaze() throws Exception {
		for (int i = 0; i < Frame.mazeAmount; i++) {
			GenMaze genMaze = new GenMaze();

			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("Maze");
			for (int row = 0; row < GenMaze.mazeDimension; row++) {
				for (int column = 0; column < GenMaze.mazeDimension; column++) {
					Element Cell = root.addElement("cell");
					Cell.addElement("X").addText(Integer.toString(column));
					Cell.addElement("Y").addText(Integer.toString(row));
					Cell.addElement("S").addText(Integer.toString(genMaze.getCell(row, column).getS()));
					Cell.addElement("CellType").addText(genMaze.getCell(row, column).getCellType());
				}
			}

			Element StartTarget = root.addElement("StartTarget");
			StartTarget.addElement("StartX").addText(Integer.toString(genMaze.getStartCell().getX()));
			StartTarget.addElement("StartY").addText(Integer.toString(genMaze.getStartCell().getY()));
			StartTarget.addElement("TargetX").addText(Integer.toString(genMaze.getTargetCell().getX()));
			StartTarget.addElement("TargetY").addText(Integer.toString(genMaze.getTargetCell().getY()));

			// Store xml file
			FileWriter fw = new FileWriter("./file/" + i + ".xml"); // file
			OutputFormat of = new OutputFormat(); // format XML
			of.setIndentSize(4); // tab = 4 spaces
			of.setNewlines(true);// auto /n
			XMLWriter xw = new XMLWriter(fw, of);
			xw.write(document);
			xw.close();
		}
	}
}