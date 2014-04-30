package main;
/**
 * @author Dean Leitersdorf, William Lee, Ophir Sneh
*/

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Program {
	ArrayList<JFrame> frames = new ArrayList<JFrame>();
	ArrayList<String> framesId = new ArrayList<String>();

	JFrame initialF = new JFrame(); // TODO: use a more descriptive variable name
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// TODO: make constants file and move constants
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	final int DISPLAY_WIDTH = (int) (screenSize.getWidth()/1.2);
	final int DISPLAY_HEIGHT = (int) (screenSize.getHeight()/1.2);
	final int xOffSet = 10;
	final int yOffSet = xOffSet;

	Program() {
		initialF.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initialF.setLayout(null);
		//initialF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialF.setTitle("Particles in Electric Field Simulator");
		initialF.getContentPane().setBackground(new Color(96,96,96));

		Display initialD = createDisplay(xOffSet, yOffSet, DISPLAY_WIDTH - (2*xOffSet),
			DISPLAY_HEIGHT - (2*yOffSet), initialF, this);

		initialF.add(initialD);
		initialF.setVisible(true);
		initialF.setResizable(false);
	}

	public Display createDisplay(int x, int y, int weight, int height, JFrame f, Program p) {
		//Not using x and y offsets for now.
		return new initialDisplay(weight, height, f, p);
	}

	public void createJFrame(double percentageOfScreenWidth, double percentageOfScreenHeight,
			String title, Color background, Boolean resizable, final String id){

		final JFrame retVal = new JFrame();
		// TODO: add comments
		retVal.setSize((int)(DISPLAY_WIDTH*percentageOfScreenWidth/100), (int)(DISPLAY_HEIGHT*percentageOfScreenHeight/100));
		retVal.setLayout(null);
		retVal.setTitle(title);
		retVal.getContentPane().setBackground(background);
		retVal.setResizable(resizable);
		retVal.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	framesId.remove(id);
		    	frames.remove(retVal);
		    }
		});
		retVal.setVisible(true);
		framesId.add(id);
		frames.add(retVal);
	}

	public JFrame getJFrameById(String id) {
		int i = framesId.indexOf(id);

		if(i >= 0 ) {
			return frames.get(i);
		}
		return null;
	}

	public void closeAllFrames(){//Closes all frames except initialF
		for(JFrame f : frames){
			f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		}
	}
}
