package groupod.application;
import groupod.algorithms.FeatureExtractor;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;



// TODO: Auto-generated Javadoc
/**
 * The class provide a graphic/picture view of feature extraction result.
 * @author changsu
 */
public class GraphicWindow {
	
	/** The frame. */
	private JFrame frame;
	
	/** The view. */
	private JPanel view;
	
	/** The categories. */
	private JPanel categories;
	
	/** The images. */
	private JPanel images;
	
	/** The raisedetched. */
	private Border raisedetched;
	
	/** The title. */
	private TitledBorder title;
	
	/** The sp. */
	private JScrollPane sp;
	
	/** The Constant MAX_TAGS. */
	private static final int MAX_TAGS = 7;
	
	/** The imag panels. */
	private static ArrayList<ImagePanel> imagPanels;
	
	/**
	 * Instantiates a new graphic window.
	 */
	public GraphicWindow(){
		imagPanels = new ArrayList<ImagePanel>();
		frame = new JFrame("Graphic/Picture View");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(900, 600));
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		// Add the view panel
		view = new JPanel();
		view.setPreferredSize(new Dimension(400,500));
		view.setLayout(new FlowLayout());
		title = BorderFactory.createTitledBorder("Graphic View");
		title.setTitleJustification(TitledBorder.CENTER);
		view.setBorder(title);
		frame.add(view, BorderLayout.CENTER);
		
		// Add the category panel
		createCategoryBtns(GroupodWindow.CATEGORIES_SPACE, GroupodWindow.UNSTRUCT_DATA_SPACE, "Space");
		createCategoryBtns(GroupodWindow.CATEGORIES_STYLE, GroupodWindow.UNSTRUCT_DATA_STYLE, "Style");
		
		// Add images panel
		images = new JPanel();
		//images.setPreferredSize(new Dimension(300,200));
		images.setLayout(new FlowLayout());
		sp = new JScrollPane(images);
		//sp.setPreferredSize(new Dimension(800,500));
		frame.add(sp, BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setVisible(true);
	}

	private void createCategoryBtns(String[] categoryNames, String filename, String titlename) {
		JPanel categoryBtns = new JPanel();
		final String fileName = filename;
		categoryBtns.setLayout(new FlowLayout());
		for(int i = 0; i < categoryNames.length; i++){
			final String btnName = categoryNames[i];
			JButton btn = new JButton(btnName);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					imagPanels.clear();
					FeatureExtractor extractor = new FeatureExtractor(fileName, btnName);
					imagPanels = extractor.getImagePanels(MAX_TAGS);
					drawImagPanels();
				}
			});
			categoryBtns.add(btn);
		}
		title = BorderFactory.createTitledBorder(titlename);
		title.setTitleJustification(TitledBorder.CENTER);
		categoryBtns.setBorder(title);
		view.add(categoryBtns);
	}
	
	/**
	 * Draw multiple image panels with top tags as labels.
	 */
	private void drawImagPanels(){
		images.removeAll();
		for(int i = 0; i < imagPanels.size(); i++){
			ImagePanel imagePanel = imagPanels.get(i);
			
			JPanel panel = new JPanel();
			//panel.setPreferredSize(new Dimension(200,200));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			
			ImageIcon icon = new ImageIcon(imagPanels.get(i).getImagPath()); 
			JLabel label = new JLabel();
			label.setIcon(icon);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			label.setPreferredSize(new Dimension(300,300));
			panel.add(label);
			panel.add(new Label(imagePanel.getTopNouns()));
			panel.add(new Label(imagePanel.getTopAdjs()));
			panel.add(Box.createRigidArea(new Dimension(0,20)));
			images.add(panel);
		}
		frame.validate();
		frame.repaint();
	}
}
