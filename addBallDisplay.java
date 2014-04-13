

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class addBallDisplay extends Display{
	
	private final int ballX;
	private final int ballY;
	private final initialDisplay initialDisplay;
	private final int pendingBallArraySizeBeforeAddingOurBall;//Describes the amount of balls pending before our ball - used when drawing the "TEMPORARY BALL" when this window is open

	public addBallDisplay(int w, int h, JFrame f, Program program, int ballX, int ballY, initialDisplay initialDisplay, int pendingBallArraySize) {
		super(w, h, f, program);
		this.ballX = ballX;
		this.ballY = ballY;
		this.initialDisplay = initialDisplay;
		this.pendingBallArraySizeBeforeAddingOurBall = pendingBallArraySize;
		init();
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		
		String[] startStrs = {"Add New Ball"};		
		Button ballAdd = new Button( new addBallCommand(hostFrame,this.initialDisplay, 0.00010, ballX, ballY, 0, 0, Math.max((Math.random()*100/1000000), 200/1000000), pendingBallArraySizeBeforeAddingOurBall), startStrs);
		ballAdd.setBounds(width/2-50, height*7/9, 100, 50);
		add(ballAdd);
		ballAdd.setVisible(true);
		
		
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	
	


	


}

