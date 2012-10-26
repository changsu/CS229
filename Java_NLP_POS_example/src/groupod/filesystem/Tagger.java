package groupod.filesystem;

import java.io.*;
import java.util.*;
import edu.stanford.nlp.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

// TODO: Auto-generated Javadoc
/**
 * The Tagger class is used to extract part of speech tags from user generated content
 * and automatically sort them based on frequency decreasingly.
 *
 * @author changsu
 */
public class Tagger {
	
	/** The Constant taggerName. */
	private static final String taggerName = "english-left3words-distsim.tagger";
	
	/** The tagger. */
	private static MaxentTagger tagger;
	
	/** The nouns. */
	private HashMap<String, Integer> nouns;
	
	/** The adjectives. */
	private HashMap<String, Integer> adjectives;
	
	/**
	 * Default constructor.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	public Tagger() throws IOException, ClassNotFoundException{
		tagger = new MaxentTagger(taggerName);
		nouns = new HashMap<String, Integer>();
		adjectives = new HashMap<String, Integer>();
	}
	
	/**
	 * Given a text of UGC, extract nouns and adjectives.
	 *
	 * @param content the content
	 */
	public void extractTags(String content){
		nouns.clear();
		adjectives.clear();
		String result = tagger.tagString(content);
		String[] tokens = result.split(" ");
		for(int i = 0; i < tokens.length; i++){
			if(tokens[i].matches(".*NN")){
				int index = tokens[i].indexOf('/');
				tokens[i] = tokens[i].substring(0, index).toLowerCase();
				if(!nouns.containsKey(tokens[i])){
					nouns.put(tokens[i], 1);
				}else{
					nouns.put(tokens[i], nouns.get(tokens[i]) + 1);
				}
			}else if(tokens[i].matches(".*JJ")){
				int index = tokens[i].indexOf('/');
				tokens[i] = tokens[i].substring(0, index).toLowerCase();
				if(!adjectives.containsKey(tokens[i])){
					adjectives.put(tokens[i], 1);
				}else{
					adjectives.put(tokens[i], adjectives.get(tokens[i]) + 1);
				}
			}
		}
	}
	
	/**
	 * Gets the nouns to string.
	 *
	 * @return sorted noun tags
	 */
	public String getNounsToString(){
		return toString(nouns);
	}
	
	/**
	 * Gets the adj to string.
	 *
	 * @return sorted adjectiv tags
	 */
	public String getAdjToString(){
		return toString(adjectives);
	}
	
	/**
	 * Return string format noun tags.
	 *
	 * @param map the map
	 * @return the string
	 */
	private String toString(HashMap<String, Integer> map){
		String result = "";
		Map sortMap = sortByValue(map);
		Set set = sortMap.entrySet();
		Iterator itr = set.iterator();
		while(itr.hasNext()){
			Object entry = itr.next();
			//System.out.println(entry);
			result += entry + " ";
		}
		return result;
	}
	
	/**
	 * Sort the map based on frequency of word.
	 *
	 * @param map the map
	 * @return the map
	 */
	private Map sortByValue(HashMap<String, Integer> map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
				.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	} 
	
}