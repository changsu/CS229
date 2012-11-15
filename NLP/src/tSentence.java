import java.util.List;

import edu.stanford.nlp.ling.HasWord;


public class tSentence {
	private List<HasWord> sentence;
	private String sentenceSource; 
	private ArrayList<Relation> relations;
	
	public tSentence(List<HasWord> sentence) {
		this.sentence = sentence;
}

	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
}

public List<HasWord> getSentence() {
	return sentence;
}

public ArrayList<Relation> getRelations() {
	return relations;
}


}
