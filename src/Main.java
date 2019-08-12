import java.awt.EventQueue;
import java.io.File;

import Maze.Frame;
import Maze.StoreMaze;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File file = new File("./file");
					if(file.list().length <50){
						StoreMaze storeMaze = new StoreMaze();
						// create mazes and store them
					}		
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
