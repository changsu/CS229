 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.nlp.trees.*;

/**
 * Define feature object that describe
 * features of a relation, which will be further used 
 * to compose feature vector for training 
 * Since we're using low-level signals/features to build 
 * relation-independent model, we will not use parser here.
 * And we'll only use POS and phase chunker to build features
 */

public class Feature {
	
	private Tree e1;
	private Tree e2;
	private String sentence;
	
	/* Feature1: number of words between two NP */
	private int numWordsBtw;
	
	/* Feature2: words between(include) two NP */
	private ArrayList<Integer> words;
	
	/* Feature3: presence of POS sequence */
	private int POSSequence;
	
	/* Featue4: number of phrases in between */
	private int numPhrasesBts;
	
	/* Feature5: number of stop words in between */
	private int numStopWords;
	
	/* Feature 6: number of punctuations in between */
	private int numPuncsBtw;
	
	/* POS to the left of e1 */
	private int POSlefte1;
	
	/* POS to the right of e2 */
	private int POSrighte2;
	
	/* store word dictionary */
	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	
	public Feature(Tree e1, Tree e2, String sentence) {
		this.e1 = e1;
		this.e2 = e2;
		this.sentence = sentence;
		buildFeatures();
	}

	/**
	 * Populate each feature
	 */
	private void buildFeatures() {
		setNumWordsBtw();
		setNumStopWords();
		setNumPuncsBtw();
		setNumPhraseBtw();
		setWords();
		setPOSSequence();
		setPOSlefte1();
		setPOSrighte2();
	}
	
	private void setNumWordsBtw() {
		
	}
	
	private void setNumStopWords() {
		
	}
	
	private void setNumPuncsBtw() {
		
	}

	private void setNumPhraseBtw() {
		
	}
	
	private void setWords() {
		
	}
	
	private void setPOSSequence() {
		
	}
	
	private void setPOSlefte1() {
		
	}
	
	private void setPOSrighte2() {
		
	}
	
	public int numWordsBtw() {
		return numWordsBtw;
	}
	
	public int numStopWords() {
		return numStopWords;
	}
	
	public int numPuncsBtw() {
		return numPuncsBtw;
	}
	
	public int numPhrasesBtw() {
		return numPhrasesBts;
	}
	
	public ArrayList<Integer> getWords() {
		return words;
	}
	
	public int POSSequence() {
		return POSSequence;
	}
	
	public int POSlefte1() {
		return POSlefte1;
	}
	
	public int POSrighte2() {
		return POSrighte2;
	}
	
}
