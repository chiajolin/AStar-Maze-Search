package Maze;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import AStar.AdaptiveAStar;
import AStar.BackwardAStar;
import AStar.LargerGForwardAStar;
import AStar.SmallerGForwardAStar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;

public class Frame extends JFrame {

	private JPanel contentPane;
	public static int mazeAmount = 50; //generate 50 mazes
	private LoadMaze mazePanel;
	public static String ChosenType;
	public static MazeList mazeList;
	public static JLabel lblShowData;

	/**
	 * Create the frame.
	 */
	public Frame() {
		mazeAmount = 50; //generate 50 mazes
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 0, 3000, 760);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//generate combobox item name
		final String mazeNumber[] = new String[mazeAmount+1];
		mazeNumber[0] = "select maze";
		for(int i = 1; i < mazeNumber.length; i++){
			mazeNumber[i] = String.valueOf(i);
		}
			
		try {
			mazeList = new MazeList();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //generate mazes
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setBounds(2, 5, 130, 27);
		comboBox.setModel(new DefaultComboBoxModel(mazeNumber));
		comboBox.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent arg0) {
	        	//show different maze when changing comboBox item
	        	ChosenType = (String)comboBox.getSelectedItem();
	        	if(mazePanel != null){
	        		contentPane.remove(mazePanel);
	        	}
	        	
	        	if(ChosenType.equals(mazeNumber[0])){
	        		
	        	}
	        	else{
		        	mazePanel= mazeList.getMazeList().get(Integer.parseInt(ChosenType)-1);
		        	mazePanel.setBounds(6, 32, 1271, 700);
		        	contentPane.add(mazePanel);
	        	}
	        	contentPane.revalidate(); 
	        	contentPane.repaint(); 	        	
	        }
	    });
		contentPane.add(comboBox);		
		
		JButton btnLargerGForwardA = new JButton("LargerGForwardA*");
		btnLargerGForwardA.setBounds(127, 4, 150, 29);
		contentPane.add(btnLargerGForwardA);		
		btnLargerGForwardA.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				LargerGForwardAStar largerGForwardAStar = new LargerGForwardAStar();
			}
			
		});
		
		JButton btnSmallergforwardA = new JButton("SmallerGForwardA*");
		btnSmallergforwardA.setBounds(267, 4, 150, 29);
		contentPane.add(btnSmallergforwardA);
		btnSmallergforwardA.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				SmallerGForwardAStar smallerGForwardAStar = new SmallerGForwardAStar();
			}
			
		});
		
		JButton btnBackwardA = new JButton("BackwardA*");
		btnBackwardA.setBounds(407, 4, 150, 29);
		contentPane.add(btnBackwardA);
		btnBackwardA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BackwardAStar backwardAStar = new BackwardAStar();
			}
		});
	
		JButton btnAdaptiveA = new JButton("AdaptiveA*");
		btnAdaptiveA.setBounds(547, 4, 150, 29);
		contentPane.add(btnAdaptiveA);
		btnAdaptiveA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdaptiveAStar adaptiveAStar = new AdaptiveAStar();
			}
		});
				
		JButton btnReload = new JButton("Reload");
		btnReload.setBounds(687, 4, 150, 29);
		contentPane.add(btnReload);
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < GenMaze.mazeDimension; i++){
					for(int j = 0; j < GenMaze.mazeDimension; j++){
						mazeList.getCurrentMaze().getCell(i, j).H = 0;
						mazeList.getCurrentMaze().getCell(i, j).setKnownS(0);
						mazeList.getCurrentMaze().getCell(i, j).setCellType(" ");
					}
				}
				mazeList.getCurrentMaze().getStartCell().G = 0;
				mazeList.getCurrentMaze().getTargetCell().G = Integer.MAX_VALUE;
				mazeList.getCurrentMaze().getStartCell().setCellType("A");
				mazeList.getCurrentMaze().getTargetCell().setCellType("T");
				
				// reveal cells of the agent and around the agent, so that the algorithms work correctly
				Cell startCell=mazeList.getCurrentMaze().getStartCell();
				Cell cell[][]=mazeList.getCurrentMaze().getCellArray();
				startCell.setKnownS(startCell.getS());
				if (startCell.getY() > 0) {
						cell[startCell.getY() - 1][startCell.getX()].setKnownS(cell[startCell.getY() - 1][startCell.getX()].getS());
				}
				if (startCell.getY() < cell.length - 1) {
					// south neighbor
						cell[startCell.getY() + 1][startCell.getX()].setKnownS(cell[startCell.getY() + 1][startCell.getX()].getS());
				}
				if (startCell.getX() > 0) {
					// left neighbor
						cell[startCell.getY()][startCell.getX() - 1].setKnownS(cell[startCell.getY()][startCell.getX() - 1].getS());
				}
				if (startCell.getX() < cell.length - 1) {
					// right neighbor
						cell[startCell.getY()][startCell.getX() + 1].setKnownS(cell[startCell.getY()][startCell.getX() + 1].getS());
				}
				repaint();
			}
			
		});
				
		//label to show information
		lblShowData = new JLabel("Information");
		lblShowData.setBounds(831, -8, 455, 50);
		contentPane.add(lblShowData);
	}
}
