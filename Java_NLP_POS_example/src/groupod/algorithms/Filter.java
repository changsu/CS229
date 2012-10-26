package groupod.algorithms;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

// TODO: Auto-generated Javadoc
/**
 * The Filter class impelents the dynamic filtering process
 * @author suchang
 *
 */
public class Filter {
	
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
	
	private JPanel tagPanel;
	/*Initial Number of Tags*/ 
	private static final int INITIAL_TAG_NUM = 5;
	/*Number of Tag in each step*/
	private static final int TAB_NUM = 5;
	
	private Set<String> currentTags;
	private Set<Node> currentChildren;
	public static Set<String> tickedTags;
	
	public Filter(){
		frame = new JFrame("Filter");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(900, 600));
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		currentTags = new HashSet<String>();
		currentChildren = new HashSet<Node>();
		tickedTags = new HashSet<String>();
			
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
		filters.setPreferredSize(new Dimension(200,500));
		filters.setLayout(new BoxLayout(filters, BoxLayout.Y_AXIS));
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
					new Filter();
					frame.setVisible(false);
				}
				else{
					frame.setVisible(false);
				}
			}
		});
		filters.add(resetBtn);
		
		// Add tag panel
		tagPanel = new JPanel();
		tagPanel.setLayout(new FlowLayout());
		tagPanel.setPreferredSize(new Dimension(50,100));
		filters.add(tagPanel);
		
		JButton startBtn = new JButton("Filter");
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showEntryTags();
			}
		});
		filters.add(startBtn);
		
		// Add "show more" button
		JButton showMoreBtn = new JButton("Show More");
		showMoreBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				generateTags();
			}
		});
		filters.add(showMoreBtn);
		
		//
		frame.add(filters, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	/**
	 * List the initial entry tags
	 * Interact with UI for tick tags
	 */
	private void showEntryTags(){
		HashMap<Node, Integer> map = new HashMap<Node, Integer>();
		Set<String> keys = Category.frequentSingleItem.keySet();
		for(String key : keys){
			Node node = Category.frequentSingleItem.get(key);
			map.put(node, node.getSupport());
		}
		// Sort top frequent single item
		map = (HashMap)sortByValue(map);
		int count = 0;
		Set<Node> keys2 = map.keySet();
		for(Node key : keys2){
			if(count++ > INITIAL_TAG_NUM) break;
			// Output first set of tags in form of check box
			addTag(key.getName());
		}
		refreshFrame();
	}
	
	private void addTag(String name){
		if(currentTags.contains(name)) return;
		final JCheckBox checkBox = new JCheckBox(name);
		String tag = checkBox.getText();
		currentTags.add(tag);
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String tag = checkBox.getText();
				if(e.getStateChange() == ItemEvent.SELECTED){
					Category.frequentSingleItem.get(tag).setFlag(true);
					tickedTags.add(tag);
				}else{
					Category.frequentSingleItem.get(tag).setFlag(false);
					tickedTags.remove(tag);
				}
			}
		});
		tagPanel.add(checkBox);
		refreshFrame();
	}
	
	/*
	 * Iterate through the remaining tags, check their parents, whethey are labeld as
	 * selected. Generate parents set, look up in the local table, show up based on probablity
	 * The method should contain the process of generating next set of tags
	 */
	
	private void generateTags(){
		showImages();
		int tagNum = 0;
		// Collection children nodes of add in tags
		for(String tag : currentTags){
			for(Node child : Category.frequentSingleItem.get(tag).getChildren()){
				if(!currentTags.contains(child.getName())){
					currentChildren.add(child);
				}
			}
		}
		// For each node, get probability and generate based on the prob.
		// This part can be furter optimized by deducting current tag from children set
		for(Node node : currentChildren){
			double probability = getProb(node);
			double r = Math.random();
			if(r < probability){ // Appear with certain probability
				if(tagNum++ > TAB_NUM) return;
				addTag(node.getName());
			}
		}
	}
	
	private double getProb(Node node){
		double prob = 0.0;
		node = Category.frequentSingleItem.get(node.getName());
		Set<Node> parents = node.getParents();
		if(!parents.isEmpty()){
			Set<String> tickedParents = new HashSet<String>();
			// If the parent of the node is ticked, add in to the ticked collection
			for(Node parent : parents){
				if(Category.frequentSingleItem.get(parent.getName()).getFlag() == true){
					tickedParents.add(parent.getName());
				}
			}
			Map<Set<String>, Double> localTableMap = node.getLocalMap();
			if(!tickedParents.isEmpty()){
				prob = localTableMap.get(tickedParents);
			}
		}
		return prob;
	}
	
	/**
	 * Sort the map based on supopr of single item
	 *
	 * @param map the map
	 * @return the map
	 */
	private Map sortByValue(HashMap<Node, Integer> map) {
		// TODO this sorting function can be deleted, just use comparator is enough to compare node
		// based on their support
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
				.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	} 

	// Refresh the page
	private void refreshFrame(){
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * After user click "show more", automatically update the images
	 * Bug here
	 */
	private void showImages(){
		//TODO
		// Re-calculate score of each image based on current ticked tags
		for(Canvas canvas : Category.canvases){
			canvas.buildSelfScore(tickedTags);
		}
		
		ArrayList<Canvas> canvasList = new ArrayList<Canvas>();
		// Sort the canvases based on new score
		for(Canvas canvas : Category.canvases){
			canvasList.add(canvas);
		}
		Collections.sort(canvasList);
		
		// Re-list all the images after sorting
		images.removeAll();
		for(int i = 0; i < canvasList.size(); i++){
			addImage(canvasList.get(i).getLocalLink());
			System.out.println(canvasList.get(i).getLocalLink() + "\n");
		}
		System.out.println("-----------------------------------------------");
		refreshFrame();
	}
	
	
	private void addImage(String link) {
		ImageIcon icon = new ImageIcon(link); 
		JLabel label = new JLabel();
		label.setIcon(icon);
		label.setPreferredSize(new Dimension(150,150));
		images.add(label);
	}
	

	private void refreshImages() {
		images.validate();
		images.repaint();
	}
}

