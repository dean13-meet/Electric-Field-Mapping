package src.Main;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

abstract class Display extends JComponent {

	Program hostProgram;
	JFrame hostFrame;
	//Not Supporting offsets.
	/*
	int xOffSet;
	int yOffSet;*/
	//Note: To support offsets, change all draw methods, and change in the constructor below
	// this.setBounds(0,0,w, h); to this.setBounds(xOffSet,yOffSet,w, h);
	int width;
	int height;

	public Display(int w, int h, JFrame f, Program program) {
		//Not Supporting offsets.
		/*this.x = x;
		this.y = y;*/
		width = w;
		height = h;
		hostFrame = f;
		hostProgram = program;
		this.setBounds(0,0,width, height);
	}

	abstract void init();

	protected abstract void paintComponent(Graphics g);
}
