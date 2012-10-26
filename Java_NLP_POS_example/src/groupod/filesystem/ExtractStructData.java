package groupod.filesystem;

import java.io.*;
import java.util.*;
import java.net.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import jxl.*; 
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

// TODO: Auto-generated Javadoc
/**
 * The ExtractStructData class is only tailored to extract the structured data from houzz.com.
 * Data Schema: ID | Title | Price | Description | Link | Image Local Path
 * */

public class ExtractStructData{
	
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
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	public static void main(String[] args) throws IOException, RowsExceededException, WriteException{
		workbook = Workbook.createWorkbook(new File("StructData.xls"));
		buildMap();
		// Iterate through the map, namely, iterate throw the categories
		Set<String> keys = entryMap.keySet();
		for(String key: keys){
			writeToFile(key, entryMap.get(key));
		}
		workbook.write(); 
		workbook.close();
	}
	
	/**
	 * The method builds up the map with category as the key and corresponding link as the value.
	 */
	private static void buildMap(){
		entryMap = new HashMap<String, String>();
		entryMap.put("Accessories And Decor", "http://www.houzz.com/photos/accessories-and-decor");
		entryMap.put("Bath Products", "http://www.houzz.com/photos/bath-products");
		entryMap.put("Bedroom Products","http://www.houzz.com/photos/bedroom-products");
		entryMap.put("Fabric", "http://www.houzz.com/photos/fabric");
		entryMap.put("Floors", "http://www.houzz.com/photos/floors");
		entryMap.put("Furniture", "http://www.houzz.com/photos/furniture");
		entryMap.put("Kitchen", "http://www.houzz.com/photos/kitchen-products");
		entryMap.put("Windows and Doors", "http://www.houzz.com/photos/windows-and-doors");
		entryMap.put("Home Office Products", "http://www.houzz.com/photos/home-office-products");
		entryMap.put("Storage and Organization", "http://www.houzz.com/photos/storage-and-organization");
		entryMap.put("Lighting", "http://www.houzz.com/photos/lighting");
	}
	
	/**
	 * The method creates a new sheet for each category and fill in the sheet.
	 *
	 * @param key the key
	 * @param link the link
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws WriteException the write exception
	 * @throws RowsExceededException the rows exceeded exception
	 */
	private static void writeToFile(String key, String link) throws IOException, WriteException,			RowsExceededException {
		WritableSheet sheet = workbook.createSheet(key, sheetNumber);
		count = 0;
		currentCategory = key;
		sheetNumber = sheetNumber + 1;
		String[] attributes = {"ID","Title","Price","Description", "Link", "Image Local Path"};
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
	 * @param categorylink the categorylink
	 * @param sheet the sheet
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void readCategory(String categorylink, WritableSheet sheet) throws IOException, RowsExceededException, WriteException{
		Document doc = Jsoup.connect(categorylink).timeout(0).get();
		Elements pages = doc.getElementsByClass("pageNumber");
		for(Element page:pages){
			Element link = page.select("a").first();
			if(link != null){
				String linkHref = link.attr("href");
				parsePage(linkHref, sheet);
			}
		}
		System.out.println("Finished! Cool!");
	}


	/**
	 * The method parses multiple produce pages in one collection.
	 *
	 * @param pageLink the page link
	 * @param sheet the sheet
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void parsePage(String pageLink, WritableSheet sheet) throws IOException, RowsExceededException, WriteException {
		Document doc = Jsoup.connect(pageLink).timeout(0).get();
		Elements products = doc.getElementsByClass("itemFrame");
		for(Element product : products){
			Element productLink = product.select("a").first();
			if(productLink != null){
				String linkHref = productLink.attr("href");
				if(linkHref != null){
					parseProduct(linkHref, sheet);
				}
			}
		}
	}
	
	/**
	 * The method parses one product page, extracts structured data and fill into the sheet.
	 *
	 * @param productLink the product link
	 * @param sheet the sheet
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RowsExceededException the rows exceeded exception
	 * @throws WriteException the write exception
	 */
	private static void parseProduct(String productLink, WritableSheet sheet) throws IOException, RowsExceededException, WriteException {
		Document doc = Jsoup.connect(productLink).timeout(0).get();
		String name_price = doc.select("[style = margin-top:10px;]").first().text();
		if(name_price.contains("$")){
			String tokens[] = name_price.split("-");
			String title = tokens[0];
			String tokens1[] = tokens[1].split(" ");
			if(tokens1.length >= 2){
				String price = tokens1[1];
				if(price.matches(".*\\d.*")){ // Eliminate "bad data"
					count++;
					
					// Save images into folder
					Element image = doc.getElementsByClass("viewSpaceImage").first();
					Element imageElem = image.getElementsByTag("img").first();
					String link = imageElem.attr("src");
					String imgSavePath = "images/" + currentCategory + "_img" + count + ".jpg";
					saveImage(link, imgSavePath);
					
					// Write into spreadsheet
					String description = doc.getElementsByTag("h2").first().text();
					ArrayList<String> tuple = new ArrayList<String>();
					tuple.add(count.toString());
					tuple.add(title);
					tuple.add(price);
					tuple.add(description);
					tuple.add(productLink);
					tuple.add(imgSavePath);
					for(int i = 0; i < tuple.size(); i++){
						Label label  = new Label(i, count, tuple.get(i));
						sheet.addCell(label);
					}

				}
			}
		}
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
