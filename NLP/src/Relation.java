import java.util.ArrayList;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import edu.stanford.nlp.ling.HasWord;  
import edu.stanford.nlp.ling.Sentence;  
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * Store relation object with two entities, spans of tokens between entities
 * and label of the relation 
 */
public class Relation {
	
	private String url;
	private boolean label;
	private Tree e1, e2;
	/* spans of tokens between the two targeted entities */
	private ArrayList<Tree> tokens;
	
	/**
	 * @param e1 target entity1
	 * @param e2 target entity2
	 * @param parse full parsed tree of the sentence
	 */
	public Relation(String url, Tree e1, Tree e2, Tree parse) {
		this.url = url;
		this.e1 = e1;
		this.e2 = e2;
		tokens = new ArrayList<Tree>();
	}
	
	/**
	 * Apply set of heuristics on the relation based on the full
	 * parse tree and generate label finally
	 */
	public void applyRules() {
		
	}
	
	/**
	 * Generate feature object for the relation
	 */
	public void generateFeatures() {
		
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
		sb.append(e1.toStructureDebugString() + "\n");
		sb.append("e2: ");
		sb.append(e2.toStructureDebugString() + "\n");
		sb.append("tokens: ");
		sb.append(tokens.toString() + "\n");
		sb.append("label: ");
		sb.append(label + "\n");
		return sb.toString();
	}
	
}
