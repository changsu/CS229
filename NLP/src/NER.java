package src;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * This is the class used for name entity recognition 
 * @author changsu
 */
public class NER {

	private AbstractSequenceClassifier<CoreLabel> classifier;
	public NER() {
		String serializedClassifier = "files/english.all.3class.distsim.crf.ser.gz";
		classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}
	
	/**
	 * Given a sentence, return its NER format
	 * for instance 
	 * original sentences: "Good afternoon Rajat Raina, how are you today?"
	 * ner format: Good/O afternoon/O Rajat/PERSON Raina/PERSON,/O how/O are/O you/O today/O?/O
	 * @param sentence
	 * @return ner format of sentence
	 */
	public String runNER(String sentence) {
		return classifier.classifyToString(sentence);
	}
	

}
