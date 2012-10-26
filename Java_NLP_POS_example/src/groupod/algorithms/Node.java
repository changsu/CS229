package groupod.algorithms;
import groupod.filesystem.*;

import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * The class repsent each node/variable/tag in the graph
 * @author suchang
 *
 */
public class Node {
	/*Use the tag as the name*/
	private String name;
	private int support;
	private Position position;
	private Set<Node> parents;
	private Set<Node> children;
	private BasicTableModel localTableModel;
	private Map<Set<String>, Double> localTableMap;
	private Set<ArrayList<Node>> permutations;
	private boolean flag;
	
	public Node(String name){
		parents = new HashSet<Node>();
		children = new HashSet<Node>();
//		 Save all subset of parents assuming T/F value for each node
		permutations = new HashSet<ArrayList<Node>>();
		this.name = name;
		localTableModel = new BasicTableModel();
		localTableMap = new HashMap<Set<String>, Double>();
		flag = false;
	}
	
	public void setPosition(Position position){
		this.position = position;
	}
	
	public Position getPosition(){
		return position;
	}
	
	public String getName(){
		return name;
	}
	
	public void setSupport(int support){
		this.support = support;
	}
	
	public int getSupport(){
		return support;
	}
	
	public String toString(){
		return name + "=>" + support + "\n";
	}
	
	public void addParents(Node node){
		parents.add(node);
	}
	
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	public Set<Node> getParents(){
		return parents;
	}
	
	public void deleteParent(Node node){
		parents.remove(node);
	}
	
	public void addChildren(Node node){
		children.add(node);
	}
	
	public Set<Node> getChildren(){
		return children;
	}
	
	public void deleteChild(Node node){
		children.remove(node);
	}
	
	public String printChildren(){
		String result = "";
		for(Node child : children){
			result += child.getName() + ",";
		}
		result += "\n";
		return result;
	}

	public void buildLocalTable(){
		ArrayList<Node> rest = new ArrayList<Node>();
		for(Node parent : parents){
			rest.add(parent);
		}
		ArrayList<Node> soFar = new ArrayList<Node>();
		Subsets(soFar,rest, permutations);
		fillInTable();
		
	}
	
	public void clearLocalTable(){
		localTableModel.clear();
		localTableMap.clear();
	}

	private void fillInTable() {
		
		clearLocalTable();
		//Add in attributes
		localTableModel.addColumn("Parents");
		localTableModel.addColumn("Probability");
		
		// Add in records
		for(ArrayList<Node> permutation : permutations){
			ArrayList<String> record = new ArrayList<String>();
			if(permutation.size()== 0) continue;
			if(permutation.size() == 1){
				String nodeName = permutation.get(0).getName();
				record.add(nodeName);
				String[] nodeSetName = {nodeName, name};
				NodeSet nodeset = new NodeSet(nodeSetName);
				Double condProb = (double)Category.itemPairMap.get(nodeset) / Category.frequentSingleItem.get(nodeName).getSupport();
				record.add(condProb.toString());
				Set<String> parentSet = new HashSet<String>();
				parentSet.add(nodeName);
				localTableMap.put(parentSet, condProb);
			}else if(permutation.size() == 2){
				String[] nodeSetName = {permutation.get(0).getName(), permutation.get(1).getName()};
				record.add(Arrays.toString(nodeSetName));
				NodeSet nodeset = new NodeSet(nodeSetName);
				int denomitor = Category.itemPairMap.get(nodeset);
				nodeset.addNode(this);
				Double condProb = (double)getNodeSetSupport(nodeset) / denomitor;
				record.add(condProb.toString());
				fillInLocalMap(nodeSetName, condProb);
			}else{// If higher level
				int index = 0;
				String[] nodeSetName = new String[permutation.size()];
				for(int i = 0; i < permutation.size(); i++){
					nodeSetName[index++] = permutation.get(i).getName();
				}
				record.add(Arrays.toString(nodeSetName));
				NodeSet nodeset = new NodeSet(nodeSetName);
				int denominator = getNodeSetSupport(nodeset);
				nodeset.addNode(this);
				int numerator = getNodeSetSupport(nodeset);
				Double condProb = (double)numerator/denominator;
				record.add(condProb.toString());
				fillInLocalMap(nodeSetName, condProb);
			}
			localTableModel.addRow(record);
		}
	}
	
	private void fillInLocalMap(String[] nodeSetName, Double condProb){
		Set<String> parentName = new HashSet<String>();
		for(int i = 0; i < nodeSetName.length; i++){
			parentName.add(nodeSetName[i]);
		}
		localTableMap.put(parentName, condProb);
	}
	/**
	 * Return node set support (>=3)
	 * @return
	 */
	private int getNodeSetSupport(NodeSet nodeset){
		int support = 0;
		if(!Category.higherOrderItemSetMap.containsKey(nodeset)){
			// Count the node set support among "busckets"
			for(Canvas canvas: Category.canvases){
				if(canvas.getItemSet().containsAll(nodeset.getNames())){
					support++;
				}
			}
			// Save a collection for higher order nodeset to reduce computation
			Category.higherOrderItemSetMap.put(nodeset, support);
		}else{
			return Category.higherOrderItemSetMap.get(nodeset);
		}
		return support;
	}
	
	// Recursion
	private void Subsets(ArrayList<Node> soFar, ArrayList<Node> rest, Set<ArrayList<Node>> permutations){
		ArrayList<Node> localSoFar = new ArrayList(soFar.size());
		ArrayList<Node> localRest = new ArrayList(rest.size());
		localSoFar.addAll(soFar);
		localRest.addAll(rest);
		if(localRest.isEmpty()){
			// Create a new object and make a local copy of the so far array list
			permutations.add(localSoFar);
			return;
		}
		Node element = new Node(localRest.get(0).getName());
		localRest.remove(0);
		Subsets(localSoFar, localRest, permutations);
		localSoFar.add(element);
		Subsets(localSoFar, localRest, permutations);
	}
	
	/**
	 * Return the local table model back to the UI
	 * @return
	 */
	public BasicTableModel getLocalTable(){
		return localTableModel;
	}
	
	public Map<Set<String>, Double> getLocalMap(){
		return localTableMap;
	}
	

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
