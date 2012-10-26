package groupod.algorithms;
import java.util.*;
import javax.swing.*;

public class Position {
	private float coorX;
	private float coorY;
	
	public Position(){
		coorX = 0.0f;
		coorY = 0.0f;
	}
	
	public Position(float x, float y){
		coorX = x;
		coorY = y;
	}
	
	public float getX(){
		return coorX;
	}
	
	public float getY(){
		return coorY;
	}
}
