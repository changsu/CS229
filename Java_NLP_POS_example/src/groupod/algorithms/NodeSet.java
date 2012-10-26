package groupod.algorithms;
import java.util.*;

import javax.swing.*;

/**
 * NodeSet store one node set
 * @author changsu
 */
public class NodeSet {
	
	private Set<Node> nodeSet;
	private int support;
	private Set<String> nameStringSet;
	
	public NodeSet(){
		nameStringSet = new HashSet<String>();
		nodeSet = new HashSet<Node>();
		support = 0;
	}
	
	public NodeSet(String[] nodeset){
		nameStringSet = new HashSet<String>();
	    nodeSet = new HashSet<Node>();
		for(int i = 0; i < nodeset.length; i++){
			Node node = new Node(nodeset[i]);
			nodeSet.add(node);
		}
	}
	
	public void addNode(Node node){
		nodeSet.add(node);
	}
	
	public int size(){
		return nodeSet.size();
	}
	
	public Set<Node> getNodeSet(){
		return nodeSet;
	}
	
	public void setSupport(int support){
		this.support = support;
	}
	
	public int getSupport(){
		return support;
	}
	
	public Set<String> getNames(){
		//Set<String> result = new HashSet<String>();
		nameStringSet.clear();
		for(Node node : nodeSet){
			nameStringSet.add(node.getName());
		}
		return nameStringSet;
	}
	
	public String toString(){
		String result = "";
		for(Node node : nodeSet){
			result += node.getName() + " ";
		}
		return result;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((nodeSet == null) ? 0 : nodeSet.hashCode());
		result = PRIME * result + support;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NodeSet other = (NodeSet) obj;
		if (nodeSet == null) {
			if (other.nodeSet != null)
				return false;
		} else if (!nodeSet.equals(other.nodeSet))
			return false;
		return true;
	}
	
}
