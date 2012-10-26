package groupod.application;
import groupod.filesystem.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
/**
 * The class represents the local probability distribution table for a given node
 * @author changsu
 *
 */
public class LocalTableWindow {
	private JFrame frame;
	private JScrollPane sp;
	private static final int FRAME_WIDTH = 200;
	private static final int FRAME_HEIGHT = 200;
	private static final int SHIFT = 50;
	
	public LocalTableWindow(String nodename, int coorX, int coorY, BasicTableModel model){
		frame = new JFrame(nodename);
		frame.setLocation(coorX + SHIFT, coorY);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		JTable t = new JTable(model);
		sp = new JScrollPane(t);
		frame.add(sp);
		frame.setVisible(true);
	}
}
