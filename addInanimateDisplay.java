import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class addInanimateDisplay extends Display {


	private final initialDisplay initialDisplay;

	public addInanimateDisplay(int w, int h, JFrame f, Program program, initialDisplay initialDisplay) {
		super(w, h, f, program);

		this.initialDisplay = initialDisplay;

		init();
	}

	@Override
	void init() {

		double charge = -0.001;
		String[] startStrs = {"Add New Inanimate"};
		Button ballAdd = new Button( new addInanimateCommand(initialDisplay, this.hostFrame,hostProgram, charge,initialDisplay.verteciesOfBeingAddedInAnimate), startStrs,width/2-100, height/2-25, 200, 50);
		add(ballAdd);
		ballAdd.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {

	}
}

