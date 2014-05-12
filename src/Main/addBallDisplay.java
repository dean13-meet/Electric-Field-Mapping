package Main;
import java.awt.Graphics;
import javax.swing.JFrame;

public class addBallDisplay extends Display{

	private static final long serialVersionUID = 1L;
	private final int ballX;
	private final int ballY;
	private final initialDisplay initialDisplay;
	private final int pendingBallArraySizeBeforeAddingOurBall;
	//Describes the amount of balls pending before our ball - used when drawing the "TEMPORARY BALL" when this window is open

	public addBallDisplay(int w, int h, JFrame f, Program program, int ballX, int ballY,
			initialDisplay initialDisplay, int pendingBallArraySize) {
		super(w, h, f, program);
		this.ballX = ballX;
		this.ballY = ballY;
		this.initialDisplay = initialDisplay;
		this.pendingBallArraySizeBeforeAddingOurBall = pendingBallArraySize;
		init();
	}

	@Override
	void init() {
		String[] startStrs = {"Add New Ball"};
		Button ballAdd = new Button("ballAdd", new addBallCommand(hostFrame,this.initialDisplay, 0.00010,
					ballX, ballY, 0, 0, Math.max((Math.random()*100/1000000), 200/1000000),
					pendingBallArraySizeBeforeAddingOurBall), startStrs, width/2-100, height/2-25, 200, 50);
		add(ballAdd);
		ballAdd.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		//empty
	}
}
