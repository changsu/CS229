import java.util.ArrayList;
import edu.stanford.nlp.trees.*;

/**
 * Store relation object with two entities, spans of tokens between entities
 * and label of the relation 
 */
public class Relation {
	
	private String url;
	private boolean label;
	private Tree e1, e2;
	/* parsed sentence from where e1, e2 are selected */
	private Tree parse;
		
	/* spans of tokens between the two targeted entities */
	private ArrayList<Tree> tokens;
	
	/* feature object of the relation */
	private Feature features;
	
	/**
	 * @param e1 target entity1
	 * @param e2 target entity2
	 * @param parse full parsed tree of the sentence
	 * @param labelFlage control whether to label the relation in construction
	 */
	public Relation(String url, Tree e1, Tree e2, Tree parse, boolean labelFlag) {
		this.url = url;
		this.e1 = e1;
		this.e2 = e2;
		this.parse = parse;
		tokens = new ArrayList<Tree>();
		/* If caller want to label the relation, we will label it
		 * by calling applyRules() method
		 */
		if (labelFlag) {
			applyRules();
		}
		// generate features of the relation
		generateFeatures();
	}
	
	/**
	 * Apply set of heuristics on the relation based on the full
	 * parse tree and generate label finally
	 * @author Liangliang Zhang
	 */
	private void applyRules() {
		// CODE GOES HERE
		label = true; // or false based on above rules
	}
	
	/**
	 * Generate feature object for the relation
	 */
	private void generateFeatures() {
		features = new Feature(e1, e2, parse);
	}
	
	/**
	 * @return feature object of the relation
	 */
	public Feature getFeatures() {
		return features;
	}
	
	/**
	 * @return label of the relation true or false
	 */
	public boolean getLabel() {
		return label;
	}
	
	/**
	 * @return url of the article where the relation is extracted
	 */
	public String getURL() {
		return url;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("e1: ");
		sb.append(e1.toString() + "\n");
		sb.append("e2: ");
		sb.append(e2.toString() + "\n");
		sb.append("tokens: ");
		sb.append(tokens.toString() + "\n");
		sb.append("label: ");
		sb.append(label + "\n");
		return sb.toString();
	}
	
}
