package Maze;

import java.util.ArrayList;
import java.util.List;

public class MazeList {
	private List<LoadMaze> mazeList = new ArrayList<LoadMaze>();
	
	public MazeList() throws Exception{
		generateMazeList();
	}
	
	private void generateMazeList() throws Exception{
		for(int i = 0; i < 50; i++){
			LoadMaze maze = new LoadMaze(i);
			mazeList.add(maze);
		}
	}
	
	public List<LoadMaze> getMazeList(){
		return mazeList;		
	}
	
	public LoadMaze getCurrentMaze(){
		return mazeList.get(Integer.parseInt(Frame.ChosenType)-1);
	}
}
