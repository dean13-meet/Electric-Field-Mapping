import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

// TODO: consider splitting each class into different file, though

public abstract class ButtonCommands {
	Display d;
	ButtonCommands(Display d) {
		this.d = d;
	}

	abstract void execute(int caseNum);
}

class pauseBallMovement extends ButtonCommands {
	initialDisplay newD = (initialDisplay) d;// Done to get access to stuff in initialDisplay and not just Display


	pauseBallMovement(initialDisplay d) {
		super(d); //Useless in this place, cuz we are using an initialDisplay.
		//Only kept here if we need to use in future.
	}


	@Override
	void execute(int caseNum) {
		switch(caseNum%2){
		case 0:
			newD.ballsMoving = true;
			break;
		case 1:
			newD.ballsMoving = false;
			break;
		}
	}
}

class Reset extends ButtonCommands{
	Reset(initialDisplay d) {
		super(d); //Useless in this place, cuz we are using an initialDisplay.
		//Only kept here if we need to use in future.
	}

	@Override
	void execute(int caseNum) {
		d.removeAll();
		d.init();
	}
}


class VoltageOnOff extends ButtonCommands{
	private final initialDisplay newD = (initialDisplay) d;// Done to get access to stuff in initialDisplay and not just Display

	VoltageOnOff(initialDisplay d) {
		super(d); //Useless in this place, cuz we are using an initialDisplay.
		//Only kept here if we need to use in future.
	}

	void execute(int caseNum) {
		switch(caseNum%2){
		case 0:
			newD.drawVoltage = true;
			newD.voltageCalcing = true;
			newD.voltageBarMax.setVisible(true);
			newD.voltageBarMin.setVisible(true);
			break;
		case 1:
			newD.drawVoltage = false;
			newD.voltageCalcing = false;
			newD.voltageBarMax.setVisible(false);
			newD.voltageBarMin.setVisible(false);
			break;
		}
	}
}

class slideElasticWalls extends ButtonCommands{
	private final initialDisplay newD = (initialDisplay) d;// Done to get access to stuff in initialDisplay and not just Display
	private final Program hostProgram;
	slideElasticWalls(initialDisplay d, Program p) {
		super(d); //Useless in this place, cuz we are using an initialDisplay.
		//Only kept here if we need to use in future.
		this.hostProgram = p;
	}

	@Override
	void execute(int caseNum) {
		if(hostProgram.getJFrameById("Change Elasticity")==null) {
			final boolean ballsWhereMoving;

			if (newD.ballsMoving) {newD.getBallStart().simulateClick();ballsWhereMoving =true;}//Always pause.
			else ballsWhereMoving = false;

			hostProgram.createJFrame(50, 25, "Change Elasticity", new Color(255,153,0), false, "Change Elasticity");

			final JFrame changeElasticityF = hostProgram.getJFrameById("Change Elasticity");
			changeElasticityF.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if (ballsWhereMoving){
						if(!newD.ballsMoving)newD.getBallStart().simulateClick();
					}
					hostProgram.framesId.remove("Change Elasticity");
					hostProgram.frames.remove(changeElasticityF);
					
				}});

			Display changeElasticityD = new elasticDisplay(changeElasticityF.getWidth(), changeElasticityF.getHeight(), changeElasticityF, hostProgram, newD);
			changeElasticityF.add(changeElasticityD);


		} else {hostProgram.getJFrameById("Change Elasticity").toFront();}
	}
}

class addBallCommand extends ButtonCommands {

	private final JFrame callingFrame;
	private final initialDisplay d;
	private final double mass;
	private final double X;
	private final double Y;
	private final double xspeed;
	private final double yspeed;
	private final double charge;
	private final int pendingBallArraySizeBeforeAddingOurBall;

	addBallCommand(JFrame callingFrame, initialDisplay d, double mass, double X, double Y, double xspeed, double yspeed, double charge, int pendingBallArraySize) {
		super(d); //Useless in this place, cuz we are using an initialDisplay.
		//Only kept here if we need to use in future.
		this.callingFrame = callingFrame;
		this.d = d;
		this.mass = mass;
		this.X = X;
		this.Y = Y;
		this.xspeed = xspeed;
		this.yspeed = yspeed;
		this.charge = charge;
		this.pendingBallArraySizeBeforeAddingOurBall = pendingBallArraySize;
	}

	@Override
	void execute(int caseNum) {
		d.toAdd.add(new Ball(d, mass, (int)X, (int)Y, xspeed, yspeed, charge));
		callingFrame.dispatchEvent(new WindowEvent(callingFrame, WindowEvent.WINDOW_CLOSING));
	}
}

class addOrEditCommand extends ButtonCommands{

	private final initialDisplay newD = (initialDisplay) d;
	addOrEditCommand(Display d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		switch(caseNum%2){
		case 0:
			newD.addOrEditBoolean = false;
			//Going to edit.
			break;
		case 1:
			newD.addOrEditBoolean = true;
			//Going to add.
			break;
		}
	}
}

class updateBallCommand extends ButtonCommands {
	private final JFrame callingFrame;
	private final initialDisplay newD = (initialDisplay) d;
	private final Ball b;
	private final int ballIndex;

	updateBallCommand(JFrame callingFrame,Display d, Ball b, int ballIndex) {
		super(d);
		this.b = b;
		this.ballIndex = ballIndex;
		this.callingFrame = callingFrame;
	}
	@Override
	void execute(int caseNum) {
		b.setColor(Ball.defaultColor);
		newD.ballarray.set(ballIndex, b);
		if (b.mass == 0) {
			int index = newD.ballarray.indexOf(b);
			newD.ballarray.remove(index);
			newD.remove(newD.chargeDisplay.get(index));
			newD.chargeDisplay.remove(index);
			//newD.repaint();
			//System.out.println("dd");
		}
		callingFrame.dispatchEvent(new WindowEvent(callingFrame, WindowEvent.WINDOW_CLOSING));
	}
}

class SaveToFile extends ButtonCommands {
	private final initialDisplay newD = (initialDisplay) d;

	SaveToFile(initialDisplay d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		Scanner s = new Scanner(System.in);
		System.out.print("Please input file name: ");
		Path file = Paths.get("Save Data/" + s.next());
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {
			out.write(String.valueOf(newD.ballarray.size()) + '\n');
			for (Ball a : newD.ballarray) {
				out.write(a.toString() + '\n');
			}
			out.write("ballsMoving: " + newD.ballsMoving + '\n');
			out.write("voltageCalcing: " + newD.voltageCalcing + '\n');
			out.write("drawVoltage: " + newD.drawVoltage + '\n');
			out.write("drawBalls: " + newD.drawBalls + '\n');
			out.write("elasticWalls: " + newD.elasticity + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class LoadFromFile extends ButtonCommands {
	private final initialDisplay newD = (initialDisplay) d;

	LoadFromFile(initialDisplay d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		Path file = Paths.get("Save Data/" + newD.presetSelected);
		try (Scanner in = new Scanner(file);) {
			int n = in.nextInt();
			newD.ballarray.clear();
			for (int i = 0; i < n; i++) {
				newD.ballarray.add(new Ball(newD, in.nextDouble(), in.nextInt(), in.nextInt(),
						in.nextDouble(), in.nextDouble(), in.nextDouble()));
			}
			in.next();
			newD.ballsMoving = in.nextBoolean();
			in.next();
			newD.voltageCalcing = in.nextBoolean();
			in.next();
			newD.drawVoltage = in.nextBoolean();
			in.next();
			newD.drawBalls = in.nextBoolean();
			in.next();
			newD.elasticity = (int) in.nextDouble();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			newD.messages.addMessage("File not found", onScreenMessage.CENTER);
		}
	}
}

class ballOrWallCommand extends ButtonCommands{
	private final initialDisplay newD = (initialDisplay) d;
	ballOrWallCommand(Display d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		switch(caseNum%2){
		case 0:
			newD.ballOrWall = false;
			//Going to wall.
			break;
		case 1:
			newD.ballOrWall = true;
			//Going to ball.
			break;
		}
	}
}
class addInanimateCommand extends ButtonCommands{
	private final initialDisplay d;
	private final Program p;
	private final double charge;
	private final ArrayList<Point> v;
	private final JFrame callingFrame;

	addInanimateCommand(initialDisplay d, JFrame callingFrame, Program host, double charge, ArrayList<Point> vertices) {
		super(d);
		this.d = d;
		this.p = host;
		this.charge = charge;
		this.v = vertices;
		this.callingFrame = callingFrame;
	}

	@Override
	void execute(int caseNum) {
		d.inAnimates.add(new inanimateObject(p, d, charge, v));
		callingFrame.dispatchEvent(new WindowEvent(callingFrame, WindowEvent.WINDOW_CLOSING));
	}
}
