
import java.awt.List;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import edu.stanford.nlp.ling.HasWord;  
import edu.stanford.nlp.ling.Sentence;  
import edu.stanford.nlp.trees.*;

/**
 * This Class define article object that stores
 * article source and  list of relations extracted from each sentence
 */

public class Article {

	private String url;
	private String body;
	private ArrayList<Relation> relations;
	
	public Article(String url, String body) {
		this.url = url;
		this.body = body;
		relations = new ArrayList<Relation>();
	}

	/**
	 * @return source url of the sentence
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * @return list of relations extracted from the sentence
	 */
	public ArrayList<Relation> getRelations() {
		return relations;
	}
	
	/**
	 * wall through each sentence in the article, get full parse tree.
	 * then iterate through every sequential pair of noun phrases e1 and e2
	 * label their relationship and populate the relations list
	 */
	public ArrayList<Relation> extractRelations(LexicalizedParser lp) {
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		iterator.setText(body);
		int curr = iterator.first();
		int start = iterator.first();
		for (int end = iterator.next();
		    end != BreakIterator.DONE;
		    start = end, end = iterator.next()) {
		  String sentence = body.substring(start,end);
		  Tree parse = lp.apply(sentence);
		  // walk through parse and find sequential noun phrases e1, e2
		  
		}
		return relations;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("url: ");
		sb.append(url);
		sb.append("\n");
		sb.append("body: ");
		sb.append(body);
		sb.append("\n");
		return sb.toString();
	}
}
