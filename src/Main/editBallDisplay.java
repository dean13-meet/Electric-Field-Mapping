package Main;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class editBallDisplay extends Display{

	private static final long serialVersionUID = 1L;
	private final initialDisplay d;
	private final int ballIndex;
	private Ball ball;

	private JTextField xCoord, yCoord, mass, charge, dx, dy; //Fields
	private JLabel xCoordL, yCoordL, massL, chargeL, dxL, dyL; //Text before field
	private JLabel xCoordU, yCoordU, massU, chargeU, dxU, dyU; //Text after field

	public editBallDisplay(int w, int h, JFrame f, Program program, Display callingDisplay, int ballIndex) {
		super(w, h, f, program);
		this.d = (initialDisplay)callingDisplay;
		this.ballIndex = ballIndex;
		this.ball = d.ballarray.get(ballIndex);
		init();
	}

	@Override
	void init() {
		xCoordL = new JLabel("X Coordinate:");
		xCoordL.setBounds(width/3 - 175, height/8 -25, 100, 25);
		add(xCoordL);
		xCoordL.setVisible(true);

		xCoord = new JTextField(new Integer(( (int)ball.getX())).toString());
		xCoord.setBounds(width/3-50, height/8 -25, 100, 25);
		add(xCoord);
		xCoord.setVisible(true);

		xCoordU = new JLabel("Pixels");
		xCoordU.setBounds(width/3+50, height/8 -25, 100, 25);
		add(xCoordU);
		xCoordU.setVisible(true);

		yCoordL = new JLabel("Y Coordinate:");
		yCoordL.setBounds(width/3 - 175, height*2/8 -25, 100, 25);
		add(yCoordL);
		yCoordL.setVisible(true);

		yCoord = new JTextField(new Integer(( (int)ball.getY())).toString());
		yCoord.setBounds(width/3-50, height*2/8 -25, 100, 25);
		add(yCoord);
		yCoord.setVisible(true);

		yCoordU = new JLabel("Pixels");
		yCoordU.setBounds(width/3+50, height*2/8 -25, 100, 25);
		add(yCoordU);
		yCoordU.setVisible(true);


		massL = new JLabel("Mass:");
		massL.setBounds(width/3 - 175, height*3/8 -25, 100, 25);
		add(massL);
		massL.setVisible(true);

		mass = new JTextField(new Double( ( ball.getMass())).toString());
		mass.setBounds(width/3-50, height*3/8 -25, 100, 25);
		add(mass);
		mass.setVisible(true);

		massU = new JLabel("Kg  (Set Mass to 0 to delete ball)");
		massU.setBounds(width/3+50, height*3/8 -25, 300, 25);
		add(massU);
		massU.setVisible(true);


		chargeL = new JLabel("Charge:");
		chargeL.setBounds(width/3 - 175, height*4/8 -25, 100, 25);
		add(chargeL);
		chargeL.setVisible(true);

		charge = new JTextField(new Double((ball.getCharge()*1000000)).toString().substring(0 , Math.min(10 , new Double((ball.getCharge() * 1000000)).toString().length())));//Don't want too many characters after decimal
		charge.setBounds(width/3-50, height*4/8 -25, 100, 25);
		add(charge);
		charge.setVisible(true);

		chargeU = new JLabel("ÂµC");
		chargeU.setBounds(width/3+50, height*4/8 -25, 100, 25);
		add(chargeU);
		chargeU.setVisible(true);


		dxL = new JLabel("Velocity X-Comp:");
		dxL.setBounds(width/3 - 175, height*5/8 -25, 125, 25);
		add(dxL);
		dxL.setVisible(true);

		dx = new JTextField(new Double(( ball.getXSpeed())).toString());
		dx.setBounds(width/3-50, height*5/8 -25, 100, 25);
		add(dx);
		dx.setVisible(true);

		dxU = new JLabel("Pixels/Second");
		dxU.setBounds(width/3+50, height*5/8 -25, 100, 25);
		add(dxU);
		dxU.setVisible(true);


		dyL = new JLabel("Velocity Y-Comp:");
		dyL.setBounds(width/3 - 175, height*6/8 -25, 125, 25);
		add(dyL);
		dyL.setVisible(true);

		dy = new JTextField(new Double(( ball.getYSpeed())).toString());
		dy.setBounds(width/3-50, height*6/8 -25, 100, 25);
		add(dy);
		dy.setVisible(true);

		dyU = new JLabel("Pixels/Second");
		dyU.setBounds(width/3+50, height*6/8 -25, 100, 25);
		add(dyU);
		dyU.setVisible(true);

		String[] startStrs = {"Update Ball"};
		Button ballEdit = new Button("ballEdit", new updateBallCommand(hostFrame, d, ball, ballIndex ), startStrs,width/2-50, height*7/9, 100, 50);
		add(ballEdit);
		ballEdit.setVisible(true);

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
		 	ball.setX(Integer.parseInt(xCoord.getText()));
			ball.setY(Integer.parseInt(yCoord.getText()));
			ball.setMass(Double.parseDouble(mass.getText()));
			ball.setCharge(Double.parseDouble(charge.getText())/1000000);
			ball.setXSpeed(Double.parseDouble(dx.getText()));
			ball.setYSpeed(Double.parseDouble(dy.getText()));
		} catch(Exception e) {

		}
		repaint();

		//I think this is a duplicate TODO, someone check with Dean to make sure if you want to do this one
//TODO: Make sure that when moving x and y of ball, it doesn't move onto an existing ball/inanimate. If it does, return it
//it's previous position, and launch an onScreenMessage to
//saying that the ball cannot be moved into another object.


	}
}
