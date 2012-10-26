package groupod.application;

import groupod.algorithms.*;
import groupod.filesystem.*;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import javax.swing.event.*;

/**
 * This class provide view of the BN network and user control panel
 * @author changsu
 *
 */
public class BNGraphWindow {

	private JFrame frame;
	private JPanel controlPanel;
	private JTextField testTxt;
	private GraphingData graphingData;
	private JComboBox operationList;
	private JTextField headNameTxt;
	private JTextField tailNameTxt;
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 1200;
	private static final int CLICK_RANGE = 30;
	
	public BNGraphWindow(){
        frame = new JFrame("BN Graph View");
        // Add graph
        frame.setLayout(new BorderLayout());
        graphingData = new GraphingData(FRAME_WIDTH, FRAME_HEIGHT);
        frame.add(graphingData, BorderLayout.CENTER);
        // Add edge edit panel
        buildEdgeEditor();
        //printNumParents();
        // After graphing the data, build local probability table(the sequence can't be changed)
        buildTables();
        
        
        frame.addMouseListener(new MyMouseListener());
        frame.setSize(FRAME_WIDTH, FRAME_WIDTH);
        frame.setLocation(200,200);
        //frame.pack();
        frame.setVisible(true);
	}
	
	public void printNumParents(){
        // Test print out no. of parents for each node
        Set<String> keys = Category.frequentSingleItem.keySet();
        for(String key: keys){
        	Node node= Category.frequentSingleItem.get(key);
        	System.out.println(node.getName() + "->" + node.getParents().size()+"\n");
        }
        
	}
	
	private void buildEdgeEditor(){
		// Add edge editor
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new FlowLayout());
		editPanel.setPreferredSize(new Dimension(1000,30));
		editPanel.add(new Label("Edge Editor"));
		String[] oprStrings = { "Add","Delete","Reverse"};
		operationList = new JComboBox(oprStrings);
		operationList.setSelectedIndex(0);
		editPanel.add(operationList);
		editPanel.add(new Label("Head: "));
		headNameTxt = new JTextField("Type name...");
		editPanel.add(headNameTxt);
		editPanel.add(new Label("Tail: "));
		tailNameTxt = new JTextField("Type name...");
		editPanel.add(tailNameTxt);
		
		JButton submitBtn = new JButton("Done");
		submitBtn.addActionListener(new ActionListener(){
			// Modify edges and thus rebuild network structure internally
			public void actionPerformed(ActionEvent arg0){
				//TODO find rules to be deletec, remove from rule collection and also change
				// corresponding parent and children relationships among nodes
				String choice = (String)operationList.getSelectedItem();
				Node head = new Node(headNameTxt.getText());
				Node tail = new Node(tailNameTxt.getText());
				Rule rule = new Rule(head,tail);
				if(choice.equals("Add")){
					Category.rulesCollection.add(rule);
					Category.frequentSingleItem.get(head.getName()).addChildren(Category.frequentSingleItem.get(tail.getName()));
					Category.frequentSingleItem.get(tail.getName()).addParents(Category.frequentSingleItem.get(head.getName()));
				}else if(choice.equals("Delete")){
					Category.rulesCollection.remove(rule);
					Category.frequentSingleItem.get(head.getName()).deleteChild(Category.frequentSingleItem.get(tail.getName()));
					Category.frequentSingleItem.get(tail.getName()).deleteParent(Category.frequentSingleItem.get(head.getName()));
				}else if(choice.equals("Reverse")){
					Rule reversedRule = new Rule(tail, head);
					Category.rulesCollection.remove(rule);
					Category.rulesCollection.add(reversedRule);
					Category.frequentSingleItem.get(head.getName()).deleteChild(Category.frequentSingleItem.get(tail.getName()));
					Category.frequentSingleItem.get(tail.getName()).deleteParent(Category.frequentSingleItem.get(head.getName()));
					Category.frequentSingleItem.get(head.getName()).addParents(Category.frequentSingleItem.get(tail.getName()));
					Category.frequentSingleItem.get(tail.getName()).addChildren(Category.frequentSingleItem.get(head.getName()));
				}
			}
		});
		editPanel.add(submitBtn);
//		testTxt = new JTextField("Flags....");
//		editPanel.add(testTxt);

		// Add in rebuild button
		JButton rebuildBtn = new JButton("Re-build");
		rebuildBtn.addActionListener(new ActionListener(){
			// Change network UI
			public void actionPerformed(ActionEvent arg0){
		        graphingData = new GraphingData(FRAME_WIDTH, FRAME_HEIGHT);
		        frame.add(graphingData, BorderLayout.CENTER);
				buildTables();
				frame.validate();
				frame.repaint();
			}
		});
		editPanel.add(rebuildBtn);
		
		// Add in entry to the filter after rebuild the network
		JButton filterBtn = new JButton("Start Filtering");
		filterBtn.addActionListener(new ActionListener(){
			// Change network UI
			public void actionPerformed(ActionEvent arg0){
				Filter filter = new Filter();
			}
		});
		editPanel.add(filterBtn);
		
		// Test button to show node children
//		JButton printChildrenBtn = new JButton("Print Children");
//		printChildrenBtn.addActionListener(new ActionListener(){
//			// Change network UI
//			public void actionPerformed(ActionEvent arg0){
//				printChildren();
//			}
//		});
//		editPanel.add(printChildrenBtn);
		
		JButton exportToDB = new JButton("Export To Database");
		exportToDB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ExportToDB db = new ExportToDB();
				db.connect();
				db.dumpIntoIndexTable(BNWindow.selectedCategory, BNWindow.supportTxt.getText(), BNWindow.confidenceTxt.getText(), (String)BNWindow.wordTypes.getSelectedItem());
				db.dumpIntoTable();
			}
		});
		
		editPanel.add(exportToDB);
		
		frame.add(editPanel, BorderLayout.SOUTH);

	}
	
	/**
	 * Given the frequent single item list with parent infomation, 
	 * build local table model for each node
	 */
	private void buildTables(){
		Set<String> keys = Category.frequentSingleItem.keySet();
		for(String key: keys){
			Node node= Category.frequentSingleItem.get(key);
			if(node.getParents().size() > 0){// If the node has parent nodes, build local table
				node.buildLocalTable();
			}else{
				node.clearLocalTable();
			}
		}
		//testTxt.setText("Cool!");
	}
	
	// Add mouse tracker to detect node selection by the user
	private class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent me){
			Integer X = me.getX();
			Integer Y = me.getY();
			Node node = getNodeByPos(X,Y);
			if(node != null){
				BasicTableModel model = node.getLocalTable();
				LocalTableWindow newTable = new LocalTableWindow(node.getName(), X, Y, model);
			}
		}
		
		/**
		 * Given the node most near to the click point
		 * @param x
		 * @param y
		 * @return
		 */
		public Node getNodeByPos(int x, int y){
			Set<String> keys = Category.frequentSingleItem.keySet();
			for(String key:keys){
				Node node = Category.frequentSingleItem.get(key);
				if(withinRange(x,y,node)){
					return node;
				}
			}
			return null;
		}
		
		private boolean withinRange(int x, int y, Node node){
			float minX = node.getPosition().getX() - CLICK_RANGE;
			float maxX = node.getPosition().getX() + CLICK_RANGE;
			float minY = node.getPosition().getY() - CLICK_RANGE;
			float maxY = node.getPosition().getY() + CLICK_RANGE;
			if(minX <= x && x <= maxX && minY <= y && y <= maxY){
				return true;
			}else{
				return false;
			}
		}
	}
	
	// Test use: print out the children of frequent nodes
	private void printChildren(){
		Set<String> keys = Category.frequentSingleItem.keySet();
		for(String key: keys){
			Node node = Category.frequentSingleItem.get(key);
			System.out.println(node.getName() + "=>" + node.printChildren());
		}
	}
}


