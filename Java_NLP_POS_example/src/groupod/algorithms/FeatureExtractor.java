package groupod.algorithms;

import groupod.filesystem.*;
import groupod.application.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import jxl.*; 
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

// TODO: Auto-generated Javadoc
/**
 * The class reads in extracted tags in .xls file and returns the result in different 
 * format 
 * @author changsu
 */
public class FeatureExtractor {
	
	/** The workbook. */
	private Workbook workbook;
	
	/** The sheet name. */
	private String sheetName;
	
	/** The top nouns. */
	private ArrayList<String> nouns;
	
	/** The top adjs. */
	private ArrayList<String> adjs;
	
	private ArrayList<String> links;
	
	/**
	 * Constructor.
	 *
	 * @param filename the filename
	 * @param sheetName the sheet name
	 */
	public FeatureExtractor(String filename, String sheetName){
		try{
			workbook = Workbook.getWorkbook(new File(filename));
			this.sheetName = sheetName;
			nouns = new ArrayList<String>();
			adjs = new ArrayList<String>();
			links = new ArrayList<String>();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Given a model, the method read in the contant from corresponding sheet in the
	 * work book and fill in the model.
	 *
	 * @param model the model
	 */
	public void fillInTable(BasicTableModel model){
		Sheet sheet = workbook.getSheet(sheetName);
		model.clear();
		// Fill in attributes
		int[] attrPosition = {7,5,6};
		for(int i = 0; i < attrPosition.length; i++){
			Cell attr = sheet.getCell(attrPosition[i], 0);
			model.addColumn(attr.getContents());
		}
		
		// Fill in records
		for(int i = 1; i < sheet.getRows(); i++){
			ArrayList<String> record = new ArrayList<String>();
			for(int j = 0; j < attrPosition.length; j++){
				record.add(sheet.getCell(attrPosition[j], i).getContents());
			}
			model.addRow(record);
			nouns.add(sheet.getCell(attrPosition[1], i).getContents());
			adjs.add(sheet.getCell(attrPosition[2], i).getContents());
			links.add(sheet.getCell(attrPosition[0], i).getContents());
		}
		
		workbook.close();
	}
	
	/**
	 * The method builds image panel objects with top k tags as labels for
	 * each image.
	 *
	 * @param topK the top k
	 * @return array list of image panels
	 */
	public ArrayList<ImagePanel> getImagePanels(int topK){
		Sheet sheet = workbook.getSheet(sheetName);
		ArrayList<ImagePanel> imagesPanels = new ArrayList<ImagePanel>();
		
		for(int i = 1; i < sheet.getRows(); i++){
			String imagPath = sheet.getCell(7,i).getContents();
			String nouns = sheet.getCell(5,i).getContents();
			String adjs = sheet.getCell(6,i).getContents();
			ImagePanel imagPanel = new ImagePanel(imagPath, nouns, adjs, topK);
			imagesPanels.add(imagPanel);
		}
		
		return imagesPanels;
	}
	
	/**
	 * Return array list of all noun tags associated with each collection
	 * @return
	 */
	public ArrayList<String> getNouns(){
		return nouns;
	}
	
	/**
	 * Return array list of all adj. tags associated with each collection
	 * @return
	 */
	public ArrayList<String> getAdjs(){
		return adjs;
	}
	
	public ArrayList<String> getLinks(){
		return links;
	}
}
