package groupod.algorithms;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

// TODO: Auto-generated Javadoc
/**
 * The Filter class currently is used to simulate the dynamic filtering mechanism,
 * while be combined with algorithm implementation later.
 * @author suchang
 *
 */
public class FilterDemo {
	
	/** The frame. */
	private JFrame frame;
	
	/** The images. */
	private JPanel images;
	
	/** The filters. */
	private JPanel filters;
	
	/** The raisedetched. */
	private Border raisedetched;
	
	/** The title. */
	private TitledBorder title;
	
	/** The sp. */
	private JScrollPane sp;
	//private static String[] imagesStr = {"images/sandwich.jpg", "images/hamburger.jpg","images/pizza.jpg","images/fruit.jpg"};
	/** The Constant NUM_OF_IMAGES. */
	private static final int NUM_OF_IMAGES = 10;
	
	/** The current filter map. */
	private static HashMap<Integer, String[]> currentFilterMap;
	
	/** The adult filter map. */
	private static HashMap<Integer, String[]> adultFilterMap;
	
	/** The child filter map. */
	private static HashMap<Integer, String[]> childFilterMap;
	
	/** The labels map. */
	private static HashMap<String, String[]> labelsMap;
	
	/** The filter level. */
	private int filterLevel = 0;
	
	/** The root type. */
	private String rootType = "";
	
	/** The labels. */
	private String[] labels;
	
	/**
	 * Default constructor.
	 */
	public FilterDemo(){
		frame = new JFrame("Filter");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(900, 600));
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		/*Build up filter and label map*/
		buildFilterMap();
		buildLabelMap();
		
		/*Add in  images panel*/
		images = new JPanel();
		images.setPreferredSize(new Dimension(400,500));
		images.setLayout(new FlowLayout());
		title = BorderFactory.createTitledBorder("Images");
		title.setTitleJustification(TitledBorder.CENTER);
		images.setBorder(title);
		sp = new JScrollPane(images);
		frame.add(sp, BorderLayout.CENTER);
		//addImage();
		
		/*Add in filter panel*/
		filters = new JPanel();
		filters.setPreferredSize(new Dimension(100,500));
		filters.setLayout(new FlowLayout());
		title = BorderFactory.createTitledBorder("Filters");
		title.setTitleJustification(TitledBorder.CENTER);
		filters.setBorder(title);
		JButton resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int more = JOptionPane.YES_OPTION;
				more = JOptionPane.showConfirmDialog(null,
						"BOOOoooM!!! Would you want to reset?", "", JOptionPane.YES_NO_OPTION);
				if(more == JOptionPane.YES_OPTION){
					new FilterDemo();
					frame.setVisible(false);
				}
				else{
					frame.setVisible(false);
				}
			}
		});
		filters.add(resetBtn);
		// Add "Expert" Picture
		ImageIcon icon = new ImageIcon("images/expert.jpg"); 
		JLabel label = new JLabel();
		label.setIcon(icon);
		//label.setPreferredSize(new Dimension(50,50));
		filters.add(label);
		
		
		// Add root filter criteria
		String[] rootStrings = { "adults", "children" };
		final JComboBox category = new JComboBox(rootStrings);
		category.setSelectedIndex(1);
		category.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String prefix = (String) category.getSelectedItem();
				rootType = prefix;
				addImage("images/"+ prefix + "/", filterLevel);
			}
		});
		filters.add(new Label("Catagory"));
		filters.add(category);
		frame.add(filters, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	/* Utility
	 * Build the tree structure (using hash map so far);
	 */
	/**
	 * Builds the filter map.
	 */
	private void buildFilterMap(){
		// Build content of each filter for adult
		adultFilterMap = new HashMap<Integer, String[]>();
		String[] adultFlt1 = {"noble", "simple", "vintage"};
		adultFilterMap.put(0, adultFlt1);
		String[] adultFlt2 = {"brignt", "dim", "fresh"};
		adultFilterMap.put(1, adultFlt2);
		String[] adultFlt3 = {"Blue", "Green", "Pink"};
		adultFilterMap.put(2, adultFlt3);
		
		// Build content of each filter for children;
		childFilterMap = new HashMap<Integer, String[]>();
		String[] cldFlt1 = {"disney", "cute", "plaine"};
		childFilterMap.put(0, cldFlt1);
		String[] cldFlt2 = {"wood", "Metal", "..."};
		childFilterMap.put(1, cldFlt2);
		String[] cldFlt3 = {"organized", "random"};
		childFilterMap.put(2, cldFlt3);
	}
	
	/**
	 * Builds the label map.
	 */
	private void buildLabelMap(){
		labelsMap = new HashMap<String, String[]>();
		String[] adultLabels = {"Style", "Light", "Pallete"};
		labelsMap.put("adults", adultLabels);
		String[] cldLabels = {"Theme", "Material", "Layout"};
		labelsMap.put("children", cldLabels);
		
	}

	/**
	 * Adds the image.
	 *
	 * @param prefix the prefix
	 * @param filterLevel the filter level
	 */
	private void addImage(String prefix, int filterLevel) {
		images.removeAll();
		for(int i = 0; i < NUM_OF_IMAGES - filterLevel * 4; i++){
			ImageIcon icon = new ImageIcon(prefix + (i+1) + ".png"); 
			JLabel label = new JLabel();
			label.setIcon(icon);
			label.setPreferredSize(new Dimension(150,150));
			images.add(label);
		}
		if(rootType.equals("adults")){
			currentFilterMap = adultFilterMap;
		}else{
			currentFilterMap = childFilterMap;
		}
		String[] strs = currentFilterMap.get(filterLevel);
		gotoNextFilter(strs);
	}

	/**
	 * Goto next filter.
	 *
	 * @param strs the strs
	 */
	private void gotoNextFilter(String[] strs) {
		filterLevel++;
		final JComboBox style = new JComboBox(strs);
		style.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addImage("images/" + rootType + "/", filterLevel);
			}
		});
		filters.add(Box.createHorizontalStrut(5));
		labels = labelsMap.get(rootType);
		filters.add(new Label(labels[filterLevel-1]));
		filters.add(style);
		frame.validate();
		frame.repaint();
	}

}
