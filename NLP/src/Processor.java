
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import edu.stanford.nlp.ling.HasWord;  
import edu.stanford.nlp.ling.Sentence;  
import edu.stanford.nlp.trees.*;
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
	
	public Processor(String inputFileName, String outputFileName){
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		articles = new ArrayList<Article>();
		relations = new ArrayList<Relation>();
	}
	
	/**
	 * Read and parse input file of JSON string and populate 
	 * list of articles
	 */
	private void readFile() {
		File inf = new File("files/" + this.inputFileName);
		if(!inf.exists()) { 
			System.err.println("ERROR: File: "+inf.getAbsolutePath()+" does not exist");
			return;
		}
		if(!inf.canRead()) { 
			System.err.println("ERROR: File: "+inf.getAbsolutePath()+" cannot be read");
			return;
		}
		
		System.out.println("INFO: finish reading file" + inf.getAbsolutePath());
		parseJSONFile(inf);
//		printArticles();
	}
	
	/**
	 * Extract relations for each article and populate current relations list
	 */
	private void extractRelations() {
		LexicalizedParser lp = 
				LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		for (Article article : articles) {
			relations.addAll(article.extractRelations(lp));
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
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("usage: Processor inputfilename outputfilename");
			return;
		} else {
			Processor processor = new Processor(args[0], args[1]);
			processor.readFile();
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
