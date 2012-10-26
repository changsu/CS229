/*
 * 
 */
package groupod.application;

import groupod.filesystem.*;
import groupod.algorithms.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

// TODO: Auto-generated Javadoc
/**
 * The GroupodWindow provides the user interface with functions of data transfer, 
 * algorithm operation and also different views of result.
 * @author changsu
 */


public class GroupodWindow {
	
	public static final String UNSTRUCT_DATA_STYLE = "UnstructDataStyle.xls";

	public static final String UNSTRUCT_DATA_SPACE = "UnstructData.xls";

	/** The frame. */
	private static JFrame frame;
	
	/** The menu bar. */
	private static JMenuBar menuBar;
	
	/** The menu. */
	private static JMenu menu;
	
	/** The content. */
	private static JPanel content;
	
	/** The raw data. */
	private static JPanel rawdata;
	
	/** The result. */
	private static JPanel result;
	
	/** The controller. */
	private static JPanel controller;
	
	/** The title. */
	private static TitledBorder title;
	
	/** The raise etched. */
	private static Border raisedetched;
	
	/** The scroll panel. */
	private static JScrollPane sp;
	
	/** The model. */
	private static BasicTableModel model;
	
	/** The original data. */
	private static JTextArea originaldata; // Store the raw data
	
	/** The spread view button. */
	private static JButton spreadBtn;
	
	/** The graphic view button */
	private static JButton graphicBtn;
		
	/** The build network. */
	private static JButton buildBNBtn;
	
	/** The Extract Button */
	private static JButton extractBtn;
	
	/** The algorithms. */
	private static JPanel algorithms;
	
	/** The Constant CATEGORIES. */
	public static final String[] CATEGORIES_SPACE = {"Bathroom", "Bedroom", "Dining Room", "Kitchen", "Living Room", "Kids"};
	public static final String[] CATEGORIES_STYLE = {"Asian", "Contemporary","Eclectic", "Mediterranean", "Modern", "Traditional", "Tropical"};
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		frame = new JFrame("Groupod");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(1000, 700));
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		/* Add Menu Bar */
		menuBar = new JMenuBar();		
		menu = new JMenu("File");
		menuBar.add(menu);
		menu = new JMenu("Edit");
		menuBar.add(menu);
		frame.add(menuBar, BorderLayout.NORTH);
		
		/* Add Center Panel--Content*/
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(raisedetched);
			
		// Add result panel
		result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
		
		// Add table in result panel
		model = new BasicTableModel();		
		JTable t = new JTable(model);
		sp = new JScrollPane(t);
		result.add(sp);
		result.setBorder(raisedetched);
		title = BorderFactory.createTitledBorder("Result View");
		title.setTitleJustification(TitledBorder.CENTER);
		result.setBorder(title);
		content.add(result);
		frame.add(content, BorderLayout.CENTER);
		
		/* Add Right Panel--Controller */
		controller = new JPanel();
		controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));
		controller.setBorder(raisedetched);
		
		// Add data transfer panel
		JPanel dataTransfer = new JPanel();
		dataTransfer.setLayout(new FlowLayout());
		JButton importBtn = new JButton("Import Data");
		importBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Read in file
				ImportFile reader = new ImportFile();
				originaldata.append(reader.readFile());
			}
		});
		JButton exportBtn = new JButton("Export Data");
		dataTransfer.add(importBtn);
		dataTransfer.add(exportBtn);
		//JLabel fileName = new JLabel("File Name: ");
		//JLabel fileSize = new JLabel("File Size: ");
		
		title = BorderFactory.createTitledBorder("Data Transfer");
		title.setTitleJustification(TitledBorder.CENTER);
		dataTransfer.setBorder(title);
		controller.add(dataTransfer);

		// Add algorithm panel
		algorithms = new JPanel();
		algorithms.setLayout(new FlowLayout());
		
		extractBtn = new JButton("Feature Extraction");
		spreadBtn = new JButton("Spreadsheet View");
		spreadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popUpCategories();
			}
		});
		
		graphicBtn = new JButton("Graphic View");
		graphicBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GraphicWindow graphicView = new GraphicWindow();
			}
		});
		
		buildBNBtn = new JButton("Build Bayesian Belief Network");
		buildBNBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BNWindow bnWindow = new BNWindow();
			}
		});
		algorithms.add(extractBtn);
		algorithms.add(spreadBtn);
		algorithms.add(graphicBtn);
		algorithms.add(buildBNBtn);
		
		title = BorderFactory.createTitledBorder("Algorithms");
		title.setTitleJustification(TitledBorder.CENTER);
		algorithms.setBorder(title);
		algorithms.setPreferredSize(new Dimension(200,350));
		controller.add(algorithms);
		
		controller.setPreferredSize(new Dimension(200,600));
		frame.add(controller, BorderLayout.EAST);
		
		// Set frame properties
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Pop up categories.
	 */
	private static void popUpCategories(){
		// The categoris here are the same in the entryMap in ExtractUnstructData.java file
		createCategoryBtns(CATEGORIES_SPACE, UNSTRUCT_DATA_SPACE, "Space");
		createCategoryBtns(CATEGORIES_STYLE, UNSTRUCT_DATA_STYLE, "Style");
		frame.validate();
		frame.repaint();
	}

	private static void createCategoryBtns(String[] categories, String filename, String titlename) {
		JPanel categoryBtns = new JPanel();
		categoryBtns.setLayout(new FlowLayout());
		final String fileName = filename;
		for(int i = 0; i < categories.length; i++){
			final String btnName = categories[i];
			JButton btn = new JButton(btnName);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FeatureExtractor extractor = new FeatureExtractor(fileName, btnName);
					extractor.fillInTable(model);
					spreadBtn.setEnabled(false);
				}
			});
			categoryBtns.add(btn);
		}
		title = BorderFactory.createTitledBorder(titlename);
		title.setTitleJustification(TitledBorder.LEFT);
		categoryBtns.setBorder(title);
		result.add(categoryBtns);
	}
}
