package groupod.algorithms;

import java.util.*;
import javax.swing.*;
/**
 * This class keep all the tags associated with one pic/collection in a
 * hash map structure
 * @author suchang
 */
public class Canvas implements Comparable {
	
	private Map<String, Integer> map;
	private String localLink;
	private double score;
	
	public Canvas(){
		map = new HashMap<String, Integer>();
		score = 0;
	}
	
	public void setLocalLink(String localLink){
		this.localLink = localLink;
	}
	
	public String getLocalLink(){
		return localLink;
	}
	
	public double getScore(){
		return score;
	}
	
	/*
	 * Parse the content and build map structure;
	 */
	public void buildMap(String content){
		String[] tokens = content.split(" ");
		for(int i = 0; i < tokens.length; i++){
			String[] partition = tokens[i].split("=");
			String key = partition[0];
			if(partition[1].equals("")) continue;
			int value = Integer.parseInt(partition[1]);
			if(!map.containsKey(key)){
				map.put(key, value);
			}
			
		}
		
	}
	
	/**
	 * Build score internally, which will be used for the ranking later
	 * To simplify, we currently use average weighting
	 */
	public void buildSelfScore(Set<String> tickedTags){
		for(String tag : tickedTags){
			if(map.containsKey(tag)){
				score += map.get(tag);
			}
		}
	}
	
	/**
	 * Return all the tags associated with the canvas/collection/pic
	 * w/o information about internal counts;
	 * @return
	 */
	public Set<String> getItemSet(){
		return map.keySet();
	}
	
	
	public int compareTo(Object otherCanvas){
		if(!(otherCanvas instanceof Canvas)){
			throw new ClassCastException("Invalid Object");
		}
		
		double score = ((Canvas)otherCanvas).getScore();
		if(this.getScore() > score){
			return 1;
		}else if(this.getScore() < score){
			return -1;
		}else{
			return 0;
		}
		
	}
	
	
}
