package groupod.algorithms;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
/**
 * This class is used to draw BNs graph
 * @author changsu
 */
 
public class GraphingData extends JPanel {
	/*Scale the radius*/
	private static final double SCALE_FACTOR= 50;
	private static final float ARROW_RADIUS = 4;
	private static int frameWidth;
	private static int frameHeight;
	private Set<Shape> nodes;
	private Set<Shape> edges;
	private static int totalNumBusckets;
    
    // Initiate Value here
    public GraphingData(int frameWidth, int frameHeight){
    	this.frameWidth = frameWidth;
    	this.frameHeight = frameHeight;
    	nodes = new HashSet<Shape>();
    	edges = new HashSet<Shape>();
    	totalNumBusckets = Category.canvases.size();
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw frequent node
        Set<String> keys = Category.frequentSingleItem.keySet();
        for(String key : keys){
        	Node node = Category.frequentSingleItem.get(key);
        	float x = (float)(Math.floor(Math.random() * (frameWidth - SCALE_FACTOR)));
        	float y = (float)(Math.floor(Math.random() * (frameHeight - 15 * SCALE_FACTOR)));
        	float radius = (float)(node.getSupport() / (double)totalNumBusckets * SCALE_FACTOR); 
        	Shape circle = new Ellipse2D.Float(x,y, radius, radius);
        	node.setPosition(new Position(x+radius/2,y + radius/2));
        	g2.setPaint(Color.green);
        	g2.fill(circle);
        	g2.setPaint(Color.red);
        	g2.drawString(node.getName(),x,y + radius/2);
        }
        
        // Draw rules
        for(Rule rule : Category.rulesCollection){
        	Node head = Category.frequentSingleItem.get(rule.getHead().getName());
        	Node tail = Category.frequentSingleItem.get(rule.getTail().getName());
        	Position headPos = head.getPosition();
        	Position tailPos = tail.getPosition();
        	g2.setPaint(Color.blue);
        	g2.drawLine((int)headPos.getX(), (int)headPos.getY(), (int)tailPos.getX(), (int)tailPos.getY());
        	g2.setPaint(Color.black);
        	Shape circle = new Ellipse2D.Float(tailPos.getX(),tailPos.getY(), ARROW_RADIUS, ARROW_RADIUS);
        	g2.fill(circle);
        }
        
    }
}
