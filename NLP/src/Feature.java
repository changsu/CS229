 
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
	private ArrayList<Tree> e1leaves;
	private ArrayList<Tree> e2leaves;
	
	/* words and its frequency between(include) two NP */
	private HashMap<Integer, Integer> words;
	
	/* number of words between two NP, including
	 * last word of e1 and first word of e2 */
	private int numWordsBtw;
	
	/* number of stop words in between */
	private int numStopWords;
	
	/* number of Capitalization words in between 
	 * here, capitalization means the first char is capitalized*/
	private int numCapWords;
	
	/* number of punctuations in between */
	private int numPuncsBtw;
		
	/* presence of POS sequence */
	private int POSSequence;
	
	/* number of phrases in between */
	private int numPhrasesBts;

	/* POS to the left of e1 */
	private int POSlefte1;
	
	/* POS to the right of e2 */
	private int POSrighte2;
	
	/* entity type of e1 */
	private int entityType1;
	
	/* entity type of e2 */
	private int entityType2;
	
	public Feature(Tree e1, Tree e2, String sentence) {
		this.e1 = e1;
		this.e2 = e2;
		this.sentence = sentence;
		words = new HashMap<Integer, Integer>();
		// TODO: delete
		this.sentence = "Jaguar, the luxury auto maker Sold 1,214 cars in the U.S.A., which is really a break news";
		buildFeatures();
	}

	/**
	 * Populate each feature
	 */
	private void buildFeatures() {
		setWords();
		setNumPhraseBtw();
		setPOSSequence();
		setPOSlefte1();
		setPOSrighte2();
		setEntityTypes();
	}

	/**
	 * Set word list between two NPs e1 and e2, 
	 * count number of words, stop words, punctuation, capital words in between
	 * TODO: should not consider verb or nouns as in the thesis? currently, we include those
	 * TODO: there is also a potential bug here that US will be broke into two words
	 * using the Java default breakIterator
	 */
	private void setWords() {
		Integer wordIndex;
		boolean addFlag = false; // control whether to add flag to words
		
		// set last word in e1 as start word, and first word in e2 as begin word
		e1leaves = (ArrayList<Tree>) e1.getLeaves();
		e2leaves = (ArrayList<Tree>) e2.getLeaves();
		String startWord = 
				removeLastPoint(e1leaves.get(e1leaves.size() - 1).label().value());
		String endWord = 
				removeLastPoint(e2leaves.get(0).label().value());
		
		// debug usage
		startWord = "maker";
		endWord = "U.S.A";
		
		BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
		iterator.setText(sentence);
		int start = iterator.first();
		for (int end = iterator.next();
				end != BreakIterator.DONE;
				start = end, end = iterator.next()) {
			
			// get word and convert from plural form if it is
			// TODO: after getting POS, this should only apply for nouns
			String word = fromPlural(sentence.substring(start, end));
			String canonicalWord = word.toLowerCase();
			// if encounter startWord, start adding words
			if (word.equals(startWord)) {
				addFlag = true;
			}
			// eliminate empty word
			if (word.isEmpty() || word.equals(" ") || word.equals("") || !addFlag) 
				continue;
			
			// detect whether current word is punctuation
			if (word.matches("[^A-Za-z0-9]")) {
				this.numPuncsBtw++;
				continue;
			}
			
			// detect whether it's stop word
			if (Processor.stopWordDictionary.get(canonicalWord) != null) {
				this.numStopWords++;
			}
			
			// only add word if it's a valid word in dictionary
			// we also consider stop words here
			if ((wordIndex = Processor.dictionary.get(canonicalWord)) != null) {
				// if it's valid word, detect whether it's capitalized word
				if (Character.isUpperCase(word.charAt(0))) {
					this.numCapWords++;
				}
				if (words.get(wordIndex) != null) {
					words.put(wordIndex, words.get(wordIndex) + 1);
				} else {
					words.put(wordIndex, 1);
				}
				this.numWordsBtw++;
			}
			
			// if read end word, end adding words
			if (word.equals(endWord)){
				addFlag = false;
			}
			
		}
		System.out.println(this);
	}
	
	private void setNumPhraseBtw() {
		
	}
	
	private void setPOSSequence() {
		
	}
	
	private void setPOSlefte1() {
		// build POS map
		
		
		// you need to make sure it's not a punctuation POS left
		String leftWord = e1leaves.get(0).label().value();
		String rightWord = e2leaves.get(e2leaves.size() - 1).label().value();
		
		// debug usage
		leftWord = "the";
		rightWord = "S.";
	}
	
	private void setPOSrighte2() {
		
	}
	
	private void setEntityTypes() {
		
	}
	
	public HashMap<Integer, Integer> getWords() {
		return words;
	}
	
	public int getNumWordsBtw() {
		return numWordsBtw;
	}
	
	public int getNumStopWords() {
		return numStopWords;
	}
	
	public int getNumPuncsBtw() {
		return numPuncsBtw;
	}
	
	public int getNumCapBtw() {
		return numCapWords;
	}
	
	public int getNumPhrasesBtw() {
		return numPhrasesBts;
	}
	
	public int getPOSSequence() {
		return POSSequence;
	}

	public int getPOSlefte1() {
		return POSlefte1;
	}

	public int getPOSrighte2() {
		return POSrighte2;
	}
	
	public int getEntityType1() {
		return entityType1;
	}
	
	public int getEntityType2() {
		return entityType2;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("original sentence: " + sentence + "\n");
		sb.append("words between: " + words.toString() + "\n");
		sb.append("number of words: " + numWordsBtw + "\n");
		sb.append("number of stop words: " + numStopWords + "\n");
		sb.append("number of punctuations: " + numPuncsBtw + "\n");
		sb.append("number of cap words: " + numCapWords + "\n");
		return sb.toString();
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
	 * This function is used to remove last point of the words in case 
	 * Java built in BreakIterator can match with the parser
	 * Say, in BreakIterator, it will return the word "U.S.A"
	 * while in parse, it will return the word "U.S.A". We need to close the gap.
	 * @param str
	 * @return string after eliminating last point if it contains
	 */
	private String removeLastPoint(String str) {
		if(str.toLowerCase().endsWith(".")) 
			return str.substring(0, str.toLowerCase().lastIndexOf("."));
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
