 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
		words = new ArrayList<Integer>();
		buildFeatures();
	}

	/**
	 * Populate each feature
	 */
	private void buildFeatures() {
		setNumStopWords();
		setNumPuncsBtw();
		setNumPhraseBtw();
		setWords();
		setPOSSequence();
		setPOSlefte1();
		setPOSrighte2();
	}
	
	private void setNumStopWords() {
		
	}
	
	private void setNumPuncsBtw() {
		
	}

	private void setNumPhraseBtw() {
		
	}
	
	private void setWords() {
		Integer wordIndex;
		int counter = 0; // keep tracks of number of valid words in between
		boolean addFlag = false; // control whether to add flag to words
		
		// set last word in e1 as start word, and first word in e2 as begin word
		ArrayList<Tree> e1leaves = (ArrayList<Tree>) e1.getLeaves();
		ArrayList<Tree> e2leaves = (ArrayList<Tree>) e2.getLeaves();
		String startWord = e1leaves.get(e1leaves.size() - 1).label().value();
		String endWord = e2leaves.get(0).label().value();
		
		BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
		iterator.setText(sentence);
		int start = iterator.first();
		for (int end = iterator.next();
				end != BreakIterator.DONE;
				start = end, end = iterator.next()) {
			// get word and convert from plural form if it is
			String word = fromPlural(sentence.substring(start, end));
			if (word.equals(startWord)) {
				addFlag = true;
			}
			// eliminate empty word
			if (word.isEmpty() || word.equals(" ") || word.equals("") || !addFlag) 
				continue;
			
			// if read end word, do not add words
			if (word.equals(endWord)){
				addFlag = false;
			}
			
			// only add word if it's a valid word in dictionary
			if ((wordIndex = Processor.dictionary.get(word)) != null) {
				words.add(wordIndex);
				++counter;
			}
		}
		numWordsBtw = counter;
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

	/* Helper functions */
	/** 
	 * Returns the non-plural form of a plural noun like: cars -> car, children -> child, people -> person, etc. 
	 * @param str 
	 * @return the non-plural form of a plural noun like: cars -> car, children -> child, people -> person, etc. 
	 */  
	private String fromPlural(String str)  
	{  
		if(str.toLowerCase().endsWith("es") && ! shouldEndWithE(str)) 
			return str.substring(0, str.toLowerCase().lastIndexOf("es"));  
		else if(str.toLowerCase().endsWith("s")) 
			return str.substring(0, str.toLowerCase().lastIndexOf('s'));  
		else if(str.toLowerCase().endsWith("children")) 
			return str.substring(0, str.toLowerCase().lastIndexOf("ren"));  
		else if(str.toLowerCase().endsWith("people")) 
			return str.substring(0, str.toLowerCase().lastIndexOf("ople")) + "rson";  
		else return str;  
	}  

	/** 
	 * Determine whether it's plural noun
	 * @param str 
	 * @return true is the singular form of a word should end with the letter "e" 
	 */  
	private boolean shouldEndWithE(String str)  
	{  
		return str.toLowerCase().endsWith("iece")  
				|| str.toLowerCase().endsWith("ice")  
				|| str.toLowerCase().endsWith("ace")  
				|| str.toLowerCase().endsWith("ise");  
	}  
}
