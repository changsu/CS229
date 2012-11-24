package src;
import java.io.IOException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Tagger {
	
	private MaxentTagger tagger;
	
	public Tagger(){
		try {
			tagger = new MaxentTagger("files/english-left3words-distsim.tagger");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tag a particular sentence and return string of tagged words
	 * Exp. Here_RB 's_VBZ a_DT tagged_VBN string_NN ._. 
	 * Sentence: "Here's a tagged string."
	 * Return: 
	 * @param sentence
	 * @return string with tagged words
	 */
	public String tagSentence(String sentence) {
		String result;
		result = this.tagger.tagString(sentence);
		return result;
	}

	
}
