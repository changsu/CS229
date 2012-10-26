package groupod.application;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Image Panel class is used to encapsulated image path, top k noun/adj. tags
 * of the image from UGC into one.
 * @author changsu
 */
public class ImagePanel {
	
	/** The imag path. */
	private String imagPath;
	
	/** The top nouns. */
	private String topNouns;
	
	/** The top adjs. */
	private String topAdjs;
	
	/**
	 * Default constructor.
	 */
	public ImagePanel(){
		imagPath = "";
		topNouns = "";
		topAdjs = "";
	}
	
	
	/**
	 * Construct a new Image panel arguments of image local path, part-of-speech tags
	 * (nouns, adjectives) and also number of tags to show.
	 *
	 * @param imagPath the imag path
	 * @param nouns the nouns
	 * @param adjs the adjs
	 * @param topK the top k
	 */
	public ImagePanel(String imagPath, String nouns, String adjs, int topK){
		this.imagPath = imagPath;
		topNouns = parseString(nouns, topK);
		topAdjs = parseString(adjs, topK);
	}
	
	/**
	 * Parses the string.
	 *
	 * @param str the str
	 * @param topK the top k
	 * @return the string
	 */
	private String parseString(String str, int topK){
		String[] tokens = str.split("&");
		String result = "";
		int upLimit = tokens.length > topK ? topK : tokens.length;
		for(int i = 0; i < upLimit; i++){
			result += tokens[i];
		}
		return result;
	}
	
	
	/**
	 * Gets the image path.
	 *
	 * @return image local path
	 */
	public String getImagPath(){
		return imagPath;
	}
	
	/**
	 * Gets the top nouns.
	 *
	 * @return String format of top k nouns
	 */
	public String getTopNouns(){
		return topNouns;
	}
	
	/**
	 * Gets the top adjs.
	 *
	 * @return String format of top k adjectives
	 */
	public String getTopAdjs(){
		return topAdjs;
	}
}
