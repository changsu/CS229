package groupod.filesystem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;

import jxl.*; 
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

// TODO: Auto-generated Javadoc
/**
 * This class is only tailored to extract the unstructured data from houzz.com initially.
 * Data Schema: ID | Title | Designer | Link | Reviews | Image Local Path
 * */

public class ExtractUnstructData {
	
	/** The count. */
	private static Integer count;
	
	/** The sheet number. */
	private static Integer sheetNumber = 0;
	
	/** The workbook. */
	private static WritableWorkbook workbook;
	//private static WritableSheet sheet;
	/** The entry map. */
	private static HashMap<String, String> entryMap;
	
	/** The current category. */
	private static String currentCategory = "";
	
	/** The Constant MAX_SAMPLE. */
	private static final int MAX_SAMPLE = 10;
	
	/** The tagger. */
	private static Tagger tagger;
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws WriteException the write exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static void main(String[] args) throws IOException, WriteException, ClassNotFoundException{
		try {
			tagger = new Tagger();
			workbook = Workbook.createWorkbook(new File("UnstructData.xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buildMap();
		// Iterate through the map, namely, iterate throw the categories
		Set<String> keys = entryMap.keySet();
		for(String key: keys){
			writeToFile(key, entryMap.get(key));
			System.out.println("Finished! Cool!");
		}
		workbook.write(); 
		workbook.close();
	}
	
	/* Utility
	 * The method builds up the map with category as the key and corresponding link as the value
	 */
	/**
	 * Builds the map.
	 */
	private static void buildMap(){
		entryMap = new HashMap<String, String>();
		
		// Category Entry Addresses		
		entryMap.put("Bathroom", "http://www.houzz.com/photos/bathroom/");
		entryMap.put("Bedroom", "http://www.houzz.com/photos/bedroom");
		entryMap.put("Dining Room", "http://www.houzz.com/photos/dining-room");
		entryMap.put("Kitchen", "http://www.houzz.com/photos/kitchen");
		entryMap.put("Living Room", "http://www.houzz.com/photos/living-room");
		entryMap.put("Kids", "http://www.houzz.com/photos/kids");
		
//		// Style Entry Addresses
//		entryMap.put("Asian", "http://www.houzz.com/photos/asian");
//		entryMap.put("Contemporary", "http://www.houzz.com/photos/contemporary");
//		entryMap.put("Eclectic", "http://www.houzz.com/photos/eclectic");
//		entryMap.put("Mediterranean",  "http://www.houzz.com/photos/mediterranean");
//		entryMap.put("Modern", "http://www.houzz.com/photos/modern");
//		entryMap.put("Traditional", "http://www.houzz.com/photos/traditional");
//		entryMap.put("Tropical", "http://www.houzz.com/photos/tropical");
	}
	
	/**
	 * The method creates a new sheet for each category and fill in the sheet.
	 *
	 * @param key the key
	 * @param link the link
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void writeToFile(String key, String link) throws RowsExceededException, WriteException{
		WritableSheet sheet = workbook.createSheet(key, sheetNumber);
		count = 0;
		currentCategory = key;
		sheetNumber = sheetNumber + 1;
		String[] attributes = {"ID","Title","Designer","Link", "Reviews","Noun Tags", "Adj Tags", "Image Local Path"};
		// Add attributes
		for(int i = 0; i < attributes.length; i++){
			Label label = new Label(i, 0, attributes[i]);
			sheet.addCell(label); 
		}
		readCategory(link, sheet);
	}
	
	/**
	 * This method reads through all the collection pages belonging to same category.
	 *
	 * @param indexLink the index link
	 * @param sheet the sheet
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void readCategory(String indexLink, WritableSheet sheet) throws RowsExceededException, WriteException{
		Document doc;
		try {
			doc = Jsoup.connect(indexLink).timeout(0).get();
			Elements pages = doc.getElementsByClass("pageNumberSmall");
			int limit = 0;
			for(Element collection : pages){
				if(limit++ > MAX_SAMPLE) break;
				String collectionLink = collection.attr("href");
				parseCollection(collectionLink, sheet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse each collection page.
	 *
	 * @param collectionLink the collection link
	 * @param sheet the sheet
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	
	private static void parseCollection(String collectionLink,  WritableSheet sheet) throws RowsExceededException, WriteException{
		try {
			Document doc = Jsoup.connect(collectionLink).timeout(0).get();
			Elements list = doc.getElementsByClass("browseListItemXL");
			Elements selectedList = list.select("[class=noHoverLink]");
			for(Element product : selectedList){
				Element productLink = product.select("a").first();
				String link = productLink.attr("href");
				if(!link.equals("#")){
					parsePage(link, sheet);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse each product page.
	 *
	 * @param pageLink the page link
	 * @param sheet the sheet
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void parsePage(String pageLink,  WritableSheet sheet) throws RowsExceededException, WriteException{
		try {
			Document doc = Jsoup.connect(pageLink).timeout(0).get();
			count++;
			// Save images into folder
			Element image = doc.getElementsByClass("viewSpaceImage").first();
			Element imageElem = image.getElementsByTag("img").first();
			String link = imageElem.attr("src");
			String imgSavePath = "images/UnstructData/" + currentCategory + "_img" + count + ".jpg";
			saveImage(link, imgSavePath);
			
			String designer = "";
			Element designerElem = doc.select("[style=font-size:18px;letter-spacing:0.1em;]").first();
			if(designerElem != null){
				designer = designerElem.text();
			}
			
			String title = doc.select("[style=margin-top:10px;]").first().text();
			String[] tags = new String[3];
			extractReviews(doc, tags);
			
			// Write into spreadsheet
			ArrayList<String> tuple = new ArrayList<String>();
			tuple.add(count.toString());
			tuple.add(title);
			tuple.add(designer);
			tuple.add(pageLink);
			for(int i = 0; i < tags.length; i++){
				tuple.add(tags[i]);
			}
			tuple.add(imgSavePath);
			for(int i = 0; i < tuple.size(); i++){
				Label label  = new Label(i, count, tuple.get(i));
				sheet.addCell(label);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extract user reviews to a specific product.
	 *
	 * @param doc the doc
	 * @param tags the tags
	 */
	private static void extractReviews(Document doc, String[] tags){
		Elements reviews = doc.getElementsByClass("rGTextDiv");
		String result = "";
		for(Element review: reviews){
			Element comment = review.getElementsByTag("div").get(2);
			result += comment.text() + "&"; // Use & to seperate comments
		}
		tagger.extractTags(result);
		tags[0] = result;
		tags[1] = tagger.getNounsToString();
		tags[2] = tagger.getAdjToString();
	}
	
	/**
	 * Save images into file.
	 *
	 * @param imageUrl the image url
	 * @param destinationFile the destination file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
	}

}
