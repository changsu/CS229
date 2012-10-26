package groupod.application;

import groupod.algorithms.*;
import groupod.filesystem.*;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
/**
 * This class create the UI for user to have control over the BN building
 * @author suchang
 */

public class BNWindow {

	private JFrame frame;
	private JPanel bnPanel;
	private JPanel ctrlPanel;
	private JPanel tablePanel;
	private TitledBorder title;
	public static JTextField supportTxt;
	public static JTextField confidenceTxt;
	public static JComboBox wordTypes;
	public static String selectedCategory;
	
	/**
	 * Constructor
	 */
	public BNWindow(){
		frame = new JFrame("Build Bayesian Network");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(900, 600));
		
		/*Add graph*/
		
		/*Build BN panel*/
		buildBNPanel();
		/*Build control panel*/
		buildControlPanel();
		
		frame.pack();
		frame.setVisible(true);
	}

	private void buildBNPanel() {
		bnPanel = new JPanel();
		bnPanel.setPreferredSize(new Dimension(400,500));
		setBorderTitle(bnPanel, "Bayesian Network");
		
		createCategoryBtns(GroupodWindow.CATEGORIES_SPACE, GroupodWindow.UNSTRUCT_DATA_SPACE, "Space");
		createCategoryBtns(GroupodWindow.CATEGORIES_STYLE, GroupodWindow.UNSTRUCT_DATA_STYLE, "Style");
		
		frame.add(bnPanel, BorderLayout.CENTER);
		
		
	}

	private void createCategoryBtns(String[] categories, String filename, String titlename) {
		JPanel categoryBtns = new JPanel();
		final String fileName = filename;
		categoryBtns.setLayout(new FlowLayout());
		for(int i = 0; i < categories.length; i++){
			final String btnName = categories[i];
			JButton btn = new JButton(btnName);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int support = Integer.parseInt(supportTxt.getText());
					double confidence = Double.parseDouble(confidenceTxt.getText());
					selectedCategory = btnName;
					Category category = new Category(support, confidence);
					category.buildCanvases(fileName, btnName, (String)wordTypes.getSelectedItem());
					category.findSingleFrequentItem();
					category.findFrequentItemPair();
					category.findAssociationRules();
					category.drawBNGraph();
				}
			});
			categoryBtns.add(btn);
		}
		title = BorderFactory.createTitledBorder(titlename);
		title.setTitleJustification(TitledBorder.LEFT);
		categoryBtns.setBorder(title);
		bnPanel.add(categoryBtns);
	}
	
	private void buildControlPanel() {
		ctrlPanel = new JPanel();
		ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.Y_AXIS));
		ctrlPanel.setPreferredSize(new Dimension(200,600));
		setBorderTitle(ctrlPanel, "Control Panel");
		
		//Add association rules parameter control
		JLabel associateRules = new JLabel("Set Parameters");
		associateRules.setHorizontalTextPosition(JLabel.LEFT);
		ctrlPanel.add(associateRules);
		
		JPanel support = new JPanel();
		JLabel supportlbl = new JLabel("Support: ");
		supportlbl.setHorizontalTextPosition(JLabel.CENTER);
		ctrlPanel.add(supportlbl);
		supportTxt = new JTextField("40");
		support.add(supportTxt);
		ctrlPanel.add(support);
		
		JPanel confidence = new JPanel();
		JLabel confidencelbl = new JLabel("Confidence: ");
		confidencelbl.setHorizontalTextPosition(JLabel.CENTER);
		ctrlPanel.add(confidencelbl);
		confidenceTxt = new JTextField("0.8");
		confidence.add(confidenceTxt);
		ctrlPanel.add(confidence);
		
		// Add in word type control combo box
		String[] options = {"Nouns", "Adjs", "NounsAdjs"};
		JPanel wordTypeSelect = new JPanel();
		wordTypes = new JComboBox(options);
		wordTypes.setSelectedIndex(0);
		wordTypeSelect.add(wordTypes);
		ctrlPanel.add(wordTypeSelect);
		
		frame.add(ctrlPanel,BorderLayout.EAST);
	}

	private void setBorderTitle(JPanel panel, String titleName) {
		title = BorderFactory.createTitledBorder(titleName);
		title.setTitleJustification(TitledBorder.CENTER);
		panel.setBorder(title);
	}
}
