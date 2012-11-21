
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The processor is the top level controller of the whole project and
 * it has several functionalities as below: 
 * 1. Read in files/documents
 * 2. Output labeled or unlabeled output as input to MATLAB 
 * 3. Output relations to database for query
 */

public class Processor {
	 
	private String inputFileName;
	private String outputFileName;
	
	/* construct list of sentence object after parsing the JSON file */
	private ArrayList<Article> articles;
	
	/* construct list of relation object as samples */
	private ArrayList<Relation> relations;

	/* store word dictionary */
	public static HashMap<String, Integer> dictionary;
	/* store stop word dictionary */
	public static HashMap<String, Integer> stopWordDictionary;
	/* store part-of-speech dictionary */
	public static HashMap<String, Integer> POSDictionary;
	/* store part-of-speech sequence dictionary */
	public static HashMap<String, Integer> POSSequenceDictonary;
	/* store POS tagger for feature extraction */
	public static Tagger tagger;

	
	public Processor(String inputFileName, String outputFileName){
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		articles = new ArrayList<Article>();
		relations = new ArrayList<Relation>();
		tagger = new Tagger();
		dictionary = new HashMap<String, Integer>();
		stopWordDictionary = new HashMap<String, Integer>();
		POSDictionary = new HashMap<String, Integer>();
		POSSequenceDictonary = new HashMap<String, Integer>();
	}
	
	/**
	 * Read input file and return File object 
	 * @param inputFileName String input file name
	 * @return File object
	 */
	public static File readFile(String inputFileName) {
		File inf = new File("files/" + inputFileName);
		if(!inf.exists()) { 
			System.err.println("ERROR: File: "+inf.getAbsolutePath()+" does not exist");
			return null;
		}
		if(!inf.canRead()) { 
			System.err.println("ERROR: File: "+inf.getAbsolutePath()+" cannot be read");
			return null;
		}
		
		System.out.println("INFO: finish reading file" + inf.getAbsolutePath());
		return inf;
	}
	
	/**
	 * Build word dictionary for feature construction
	 */
	public void readDictionary(HashMap<String, Integer> dictionary, String fileName) {
		File inf = Processor.readFile(fileName);
		int wordIndex = 0;
		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(inf));

			String line = null;
			while((line = reader.readLine()) != null ) {
				if (dictionary.containsKey(line)) {
					continue;
				} else {
					dictionary.put(line, ++wordIndex);
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Parse the input JSON file, construct sentence object from each JSON
	 * object for further process and populate the sentence list
	 * @param inf
	 */
	private void parseJSONFile(File inf) {
		/* convert content of the file into string */
		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(inf));

			String line = null;
			StringBuilder  stringBuilder = new StringBuilder();

			while( ( line = reader.readLine() ) != null ) {
				stringBuilder.append( line );
			}
			
			// Parse the JSON string to JSON array
			JSONArray records = new JSONArray(stringBuilder.toString());
			
			// construct one article object for each JSON object in JSON array
			for (int i = 0; i < records.length(); ++i) {
				JSONObject record = records.getJSONObject(i);
				Article article = new Article(record.getString("url"), 
						record.getString("body"));
				articles.add(article);
			}			
			// close input stream
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Extract relations for each article and populate current relations list
	 */
	private void extractRelations() {
		LexicalizedParser lp = 
				LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		relations.addAll(articles.get(0).extractRelations(lp));
//		for (Article article : articles) {
//			relations.addAll(article.extractRelations(lp));
//		}
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("usage: Processor inputfilename outputfilename");
			return;
		} else {
			Processor processor = new Processor(args[0], args[1]);
			File inf = processor.readFile(processor.inputFileName);
			processor.parseJSONFile(inf);
			processor.readDictionary(dictionary, "3esl.txt");
			processor.readDictionary(stopWordDictionary, "stopword.txt");
			processor.readDictionary(POSDictionary, "pos.txt");
			processor.extractRelations();
		}
	}
	
	/**
	 * Set of utility print functions for debugging
	 */
	private void printArticles() {
		for (Article article : articles) {
			System.out.println(article.toString());
		}
	}
	
	private void printRelations() {
		for (Relation relation : relations) {
			System.out.println(relation.toString());
		}
	}
}
