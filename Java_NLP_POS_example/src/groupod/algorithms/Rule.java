package groupod.algorithms;
/**
 * The class define rules/edges for the graph
 * @author suchang
 */
public class Rule {
	private Node head;
	private Node tail;
	private double confidence;
	
	public Rule(Node head, Node tail){
		this.head = head;
		this.tail = tail;
	}
	
	public Node getHead(){
		return head;
	}
	
	public Node getTail(){
		return tail;
	}
	
	public void setConfidence(double confidence){
		this.confidence = confidence;
	}
	
	public double getConfidence(){
		return confidence;
	}
	
	public String toString(){
		return head.getName() + "->" + tail.getName() + ":" + confidence + "\n";
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((head == null) ? 0 : head.hashCode());
		result = PRIME * result + ((tail == null) ? 0 : tail.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Rule other = (Rule) obj;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (tail == null) {
			if (other.tail != null)
				return false;
		} else if (!tail.equals(other.tail))
			return false;
		return true;
	}
	
	
}
