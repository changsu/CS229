
import java.awt.List;
import java.util.ArrayList;

/**
 * This Class define sentence object that stores
 * sentence source and also list of relations
 * extracted from that sentence
 */

public class tSentence {

	private String url;
	private String body;
	private ArrayList<Relation> relations;
	
	public tSentence(String url, String body) {
		this.url = url;
		this.body = body;
		relations = new ArrayList<Relation>();
	}

	public String getURL() {
		return url;
	}
	
	public ArrayList<Relation> getRelations() {
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
