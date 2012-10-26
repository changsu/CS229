package groupod.algorithms;
import groupod.filesystem.*;
import groupod.application.*;

import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;

/**
 * This class find frequent item set and association rules 
 * @author suchang
 */
public class Category{
	
	public static Set<Canvas> canvases;
	public static Map<Node, Integer> singleItemMap;
	public static Map<NodeSet, Integer> itemPairMap;
	/*These three collection should be accessable by other classes*/
	public static Map<String, Node> frequentSingleItem;
	public static Set<NodeSet> frequentItemPair;
	public static Map<NodeSet, Integer> higherOrderItemSetMap;
	public static Set<Rule> rulesCollection;
	private ArrayList<String> nounTags;
	private ArrayList<String> adjTags;
	private ArrayList<String> links;
	private BasicTableModel model;
	private int support;
	private double confidence;
	
	public Category(int support, double confidence){
		canvases = new HashSet<Canvas>();
		singleItemMap = new HashMap<Node, Integer>();
		itemPairMap = new HashMap<NodeSet, Integer>();
		frequentSingleItem = new HashMap<String,Node>();
		frequentItemPair = new HashSet<NodeSet>();
		higherOrderItemSetMap = new HashMap<NodeSet, Integer>();
		rulesCollection = new HashSet<Rule>();
		nounTags = new ArrayList<String>();
		adjTags = new ArrayList<String>();
		links = new ArrayList<String>();
		model = new BasicTableModel();
		this.support = support;
		this.confidence = confidence;
	}
	
	public void buildCanvases(String filename, String categoryName, String wordType){
		FeatureExtractor extractor = new FeatureExtractor(filename, categoryName);
		extractor.fillInTable(model);
		ArrayList<String> currentTags = new ArrayList<String>();
		
		nounTags = extractor.getNouns();
		adjTags = extractor.getAdjs();
		links = extractor.getLinks();
		
		if(wordType.equals("Nouns")){
			currentTags.addAll(nounTags);
		}else if(wordType.equals("Adjs")){
			currentTags.addAll(adjTags);
		}else{
			for(int i = 0; i < nounTags.size(); i++){
				String temp = nounTags.get(i) + adjTags.get(i);
				currentTags.add(temp);
			}
		}
		
		for(int i = 0; i < currentTags.size(); i++){
			if(!currentTags.get(i).equals("")){
				Canvas canvas = new Canvas();
				canvas.buildMap(currentTags.get(i));
				canvas.setLocalLink(links.get(i));
				canvases.add(canvas);
			}
		}
	}
	
	public void findSingleFrequentItem(){
		// Here may be out of memory if running locally
		for(Canvas canvas : canvases){
			Set<String> itemset = canvas.getItemSet();
			for(String item : itemset){
				Node node = new Node(item);
				if(singleItemMap.containsKey(node)){
					singleItemMap.put(node, singleItemMap.get(node) + 1);
				}else{
					singleItemMap.put(node, 1);
				}
			}
		}
		//System.out.println(singleItemMap.toString());
		// Find frequent single item
		Set<Node> keys = singleItemMap.keySet();
		for(Node key: keys){
			int localSupport = singleItemMap.get(key);
			key.setSupport(localSupport);
			if(localSupport >= support){
				frequentSingleItem.put(key.getName(), key);
				//Print out frequent single item
				//System.out.println(key.toString());
			}
		}

	}
	
	// Using PYC algorithm?? name?? to reduce complexity
	// This part need to be more general, maybe
	// Second pass of the data
	public void findFrequentItemPair(){
		
		for(Canvas canvas: canvases){
			Set<String> itemset = canvas.getItemSet();
			// Filter out non-frequent single item
			ArrayList<Node> reducedSet = new ArrayList<Node>();
			for(String item: itemset){
				//Node node = new Node(item);
				if(frequentSingleItem.containsKey(item)){
					reducedSet.add(frequentSingleItem.get(item));
				}
			}
			
			// Build item pair map
			for(int i = 0; i < reducedSet.size(); i++){
				for(int j = i + 1; j < reducedSet.size(); j++){
					// can be optimized here
					String[] nodeNames = {reducedSet.get(i).getName(), reducedSet.get(j).getName()};
					NodeSet nodeSet = new NodeSet(nodeNames);
					if(itemPairMap.containsKey(nodeSet)){
						itemPairMap.put(nodeSet, itemPairMap.get(nodeSet) + 1);
					}else{
						itemPairMap.put(nodeSet, 1);
					}
				}
			}			
		}
		
		// Find frequent item pairs
		Set<NodeSet> keys = itemPairMap.keySet();
		for(NodeSet key : keys){
			int localSupport = itemPairMap.get(key);
			key.setSupport(localSupport);
			if(localSupport >= support){
				frequentItemPair.add(key);
				// Print out frequent item pair set
				//System.out.println(key.toString() + "=>" + localSupport + "\n");
			}
		}
	}
	
	/**
	 * Find association rules from frequent item pair sets
	 */
	public void findAssociationRules(){
		for(NodeSet freqItemPair: frequentItemPair){
			Set<Rule> rules = findRules(freqItemPair);
			if(rules.size()!=0){
				rulesCollection.addAll(rules);// set union
			}
		}
//		// Print out rules
//		for(Rule rule : rulesCollection){
//			System.out.println(rule.toString());
//		}
	}
	
	/**
	 * For given node set(item pair), find rules
	 * @param nodeset
	 */
	public Set<Rule> findRules(NodeSet nodeset){
		double confidenceLocal = 0;
		Set<Rule> rules = new HashSet<Rule>();
		Set<Node> nodeSet = nodeset.getNodeSet();
		ArrayList<Node> nodeArray = new ArrayList<Node>();
		for(Node node: nodeSet){
			nodeArray.add(node);
		}
		
		for(int i = 0; i < nodeArray.size(); i++){
			int pairSupport = nodeset.getSupport();
			int singleSupport = singleItemMap.get(nodeArray.get(i));
			//int otherSupport = singleItemMap.get(nodeArray.get((i+1)%2));
			confidenceLocal = (double)pairSupport / singleSupport;
			//confidenceLocal = ((double)pairSupport / singleSupport - (double)otherSupport/canvases.size()); 
			if(confidenceLocal > this.confidence){
				//Rule rule = new Rule(nodeArray.get(i), nodeArray.get((i+1)%2));
				Node head = nodeArray.get((i+1)%2);
				Node tail = nodeArray.get(i);
				Rule rule = new Rule(head, tail);
				rule.setConfidence(confidenceLocal);
				rules.add(rule);
				// Set parent and children relationship
				frequentSingleItem.get(tail.getName()).addParents(head);
				frequentSingleItem.get(head.getName()).addChildren(tail);
			}
		}
		return rules;
	}
	
	/**
	 * Draw each node in the frequent single item set
	 */
	public void drawBNGraph(){
		BNGraphWindow bnGraph = new BNGraphWindow();
	}
	
}