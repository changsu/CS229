 
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

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
	
	/* pos dictionary, key: word value: POS tag */
	private HashMap<String, String> posDic;
	
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
		e1leaves = (ArrayList<Tree>) e1.getLeaves();
		e2leaves = (ArrayList<Tree>) e2.getLeaves();
		words = new HashMap<Integer, Integer>();
		posDic = new HashMap<String, String>();
		// TODO: delete
		this.sentence = "Jaguar, the luxury auto maker Sold 1,214 cars in the U.S.A.";
		buildFeatures();
	}

	/**
	 * Populate each feature
	 */
	private void buildFeatures() {
		setPOSSequence();
		setWords();
		setNumPhraseBtw();
		setEntityTypes();
	}
	
	/**
	 * This function tag the POS of each word in the sentence
	 * and generate feature of POS sequence between e1 and e2
	 * and also POS of the word before e1 and POS of the word after e2
	 * We have normal POS tags pre-stored in the dictionary. 
	 * While for the feature of POS sequence, we will build the dictionary along with each
	 * new sequence we have encountered
	 */
	private void setPOSSequence() {
		// build POS arrayList during which process generate POS features
		String taggedSentence = Processor.tagger.tagSentence(sentence);
		
		// get the left word of e1 and right word of e2
		String leftWord = e1leaves.get(0).label().value();
		String rightWord = e2leaves.get(e2leaves.size() - 1).label().value();
		
		// debug use
		leftWord = "the";
		rightWord = "U.S.A.";
		
		// store left word of e1 and right word of e2 
		String leftToE1 = "", rightToE2 = "";
		// flags avoid repeated assignment to lefttoe1 or righttoe2
		boolean flagLeftToE1 = false, flagRightToE2 = false;
		String previouWord = "";
		
		// walk through taggedSentence, build posDic and also find leftToE1 and rightToE2
		StringTokenizer st = new StringTokenizer(taggedSentence);
		while (st.hasMoreTokens()) {
			String currWord = st.nextToken();
			// further tokenize each tagged words into original word and its tag
			StringTokenizer subSt = new StringTokenizer(currWord, "_");
			String word = subSt.nextToken();
			String tag = subSt.nextToken();
			
			// we do not store punctuation in the posDic
			if (word.matches("[^A-Za-z0-9]")) 
				continue;
			// if current words is leftword of e1, then lefttoe1 is previousWord
			if (word.equals(leftWord) && !flagLeftToE1) { 
				leftToE1 = previouWord;
				flagLeftToE1 = true;
			}
			// if previousWord is rightword of e2, then righttoe2 is word
			if (previouWord.equals(rightWord) && !flagRightToE2) { 
				rightToE2 = word;
				flagRightToE2 = true;
			}
			previouWord = word;
			posDic.put(word, tag);
		}
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
			String word = sentence.substring(start, end);
			String tag = posDic.get(word);
			if (tag != null && tag.matches("NN.*"))
				word = fromPlural(word);
			
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
