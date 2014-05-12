package src.Main;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

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
			newD.ballsMoving = false;
			break;
		case 1:
			newD.ballsMoving = true;
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
			newD.drawVoltage = false;
			newD.voltageCalcing = false;
			newD.voltageBarMax.setVisible(false);
			newD.voltageBarMin.setVisible(false);
			break;
		case 1:
			newD.drawVoltage = true;
			newD.voltageCalcing = true;
			newD.voltageBarMax.setVisible(true);
			newD.voltageBarMin.setVisible(true);
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
		if (hostProgram.getJFrameById("Change Elasticity") == null) {
			final boolean ballsWhereMoving;

			if (newD.ballsMoving) {newD.getBallStart().simulateClick();ballsWhereMoving = true;}//Always pause.
			else ballsWhereMoving = false;

			hostProgram.createJFrame(50, 25, "Change Elasticity", new Color(255,153,0), false, "Change Elasticity");

			final JFrame changeElasticityF = hostProgram.getJFrameById("Change Elasticity");
			changeElasticityF.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if (ballsWhereMoving) {
						if (!newD.ballsMoving) newD.getBallStart().simulateClick();
					}
					hostProgram.framesId.remove("Change Elasticity");
					hostProgram.frames.remove(changeElasticityF);
				}});

			Display changeElasticityD = new elasticDisplay(changeElasticityF.getWidth(), changeElasticityF.getHeight(), changeElasticityF, hostProgram, newD);
			changeElasticityF.add(changeElasticityD);

		}
		else {hostProgram.getJFrameById("Change Elasticity").toFront();}
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
	}

	@Override
	void execute(int caseNum) {
		d.toAdd.add(new Ball(d, mass, (int)X, (int)Y, xspeed, yspeed, charge));
		callingFrame.dispatchEvent(new WindowEvent(callingFrame, WindowEvent.WINDOW_CLOSING));
	}
}
//TODO: Make sure that when adding a ball, there is no inanimate in the location it is being added.

class addOrEditCommand extends ButtonCommands{

	private final initialDisplay newD = (initialDisplay) d;
	addOrEditCommand(Display d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		Button b = newD.getButtonByName("toolButton");
		newD.tool = b.getText().replace("Tool: ", "");
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
		File directory = new File("Save Data/file_");
		Path file = Paths.get(directory.getAbsolutePath() + s.next());
		System.out.println(file);
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {
			out.write("Original Width: " + newD.hostProgram.DISPLAY_WIDTH + "\n");
			out.write("Original Height: " + newD.hostProgram.DISPLAY_HEIGHT + "\n");
			out.write(String.valueOf(newD.ballarray.size()) + '\n');
			for (Ball a : newD.ballarray) {
				out.write(a.toString() + '\n');
			}
			out.write(String.valueOf(newD.inAnimates.size()) + "\n");
			for(inanimateObject i : newD.inAnimates){
				out.write(i.toString() + "\n");
			}
			out.write("ballsMoving: " + newD.ballsMoving + '\n');
			out.write("voltageCalcing: " + newD.voltageCalcing + '\n');
			out.write("drawVoltage: " + newD.drawVoltage + '\n');
			out.write("drawBalls: " + newD.drawBalls + '\n');
			out.write("elasticWalls: " + newD.elasticity + '\n');

			//Saving buttons: (Note: When saving buttons we only need to save the # of times they were
			//clicked, and then we restore them with that number -1, and simulate a click on them!)

			out.write(newD.buttons.size() + "\n");
			for(Button b : newD.buttons){
				out.write(b.name + " " + b.timesClicked + "\n");
			}
		}

		catch (IOException e) {
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
		File directory = new File("Save Data/file_");
		Path file = Paths.get(directory.getAbsolutePath() + newD.getPresetSelected());

		try (Scanner in = new Scanner(file);) {
			while(!in.hasNextInt()){//Scrolls past text to the next int
				in.next();
			}
			int originalWidth = in.nextInt();
			while (!in.hasNextInt()){
				in.next();
			}
			int originalHeight = in.nextInt();
			final double widthRatio = (double)newD.hostProgram.DISPLAY_WIDTH/(double)originalWidth;
			final double heightRatio = (double)newD.hostProgram.DISPLAY_HEIGHT/(double)originalHeight;
			int numberBalls = in.nextInt();
			newD.ballarray.clear();
			for (int i = 0; i < numberBalls; i++) {

				newD.ballarray.add(new Ball(newD,
						in.nextDouble(),
						(int) (in.nextDouble()*widthRatio),
						(int) (in.nextDouble()*heightRatio),
						in.nextDouble(), in.nextDouble(), in.nextDouble()));
			}
			//Do Labels:
			for (JLabel c : newD.chargeDisplay) {
				newD.remove(c);
			}
			newD.chargeDisplay.clear();
			for (int i = 0; i < newD.ballarray.size(); i++) {
				JLabel l = new JLabel();
				newD.chargeDisplay.add(l);
				newD.add(l);
				l.setVisible(true);
			}

			int numberInanimates = in.nextInt();
			newD.inAnimates.clear();
			for (int i = 0; i < numberInanimates; i++) {
				class vert {
					ArrayList<Point> getVertecies(){
						ArrayList<Point> retval = new ArrayList<Point>();
						int numberVertecies = in.nextInt();
						for(int i = 0 ; i < numberVertecies; i ++){
							retval.add(new Point((int)(in.nextInt() * widthRatio), (int)(in.nextInt() * heightRatio)));
						}
						return retval;
					}
				}
				newD.inAnimates.add(new inanimateObject(newD.hostProgram, newD, in.nextDouble(), new vert().getVertecies()));
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

			//Load button states:
			int numberButtons = in.nextInt();
			if (newD.buttons.size() != numberButtons) {
				newD.messages.addMessage("Error: Preset does not contain proper amount of GUI buttons. Will not load buttons!", onScreenMessage.CENTER);
				return;
			}
			ArrayList<String> buttonsToIgnore = new ArrayList<String>();
			buttonsToIgnore.add("reset");
			buttonsToIgnore.add("elasticWallsButton");
			buttonsToIgnore.add("saveToFile");
			buttonsToIgnore.add("loadFromFile");
			for(int i = 0; i < numberButtons; i++) {
				String buttonName = in.next();
				newD.getButtonByName(buttonName).timesClicked = in.nextInt() - 1;
				if(!buttonsToIgnore.contains(buttonName))
				newD.getButtonByName(buttonName).simulateClick();

			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			newD.messages.addMessage("File not found", onScreenMessage.CENTER);
		}
	}
}

class ballOrWallCommand extends ButtonCommands {
	private final initialDisplay newD = (initialDisplay) d;
	ballOrWallCommand(Display d) {
		super(d);
	}

	@Override
	void execute(int caseNum) {
		Button b = newD.getButtonByName("typeButton");
		newD.type = b.getText().replace("Type: ", "");

		Button tool = newD.getButtonByName("toolButton");//After changing the type, we will change the tool button to match the appropriate tools!
		if (newD.type.equals("Ball")) {
			String[] toolStrings = new String[newD.ballTools.length];
			for(int i = 0; i < toolStrings.length; i++){
				toolStrings[i] = "Tool: " + newD.ballTools[i];
			}
			tool.strs = toolStrings;
			tool.roundLength = toolStrings.length;
			tool.timesClicked = -1;
			//Now, set the tool being used to match one of the optional tools available
			tool.simulateClick();
		}
		else if (newD.type.equals("Inanimate")) {
			String[] toolStrings = new String[newD.inanimateTools.length];
			for (int i = 0; i < toolStrings.length; i++) {
				toolStrings[i] = "Tool: " + newD.inanimateTools[i];
			}
			tool.strs = toolStrings;
			tool.roundLength = toolStrings.length;
			tool.timesClicked = -1;
			//Now, set the tool being used to match one of the optional tools available
			tool.simulateClick();
		}
		else if (newD.type.equals("Arrow")) {
			String[] toolStrings = new String[newD.arrowTools.length];
			for (int i = 0; i < toolStrings.length; i++) {
				toolStrings[i] = "Tool: " + newD.arrowTools[i];
			}
			tool.strs = toolStrings;
			tool.roundLength = toolStrings.length;
			tool.timesClicked = -1;
			//Now, set the tool being used to match one of the optional tools available
			tool.simulateClick();
		}
	}
}

class addInanimateCommand extends ButtonCommands {
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
		//TODO
		/*
		 * Make sure no overlapping inanimates:
		 * Check to see if there is an inanimate at the location before adding this new one
		 *
		 * To do this, loop over all inanimates, and determine if any of the lines of
		 * the inanimate being added intersects any of the lines of any of the
		 * existing inanimates.
		 *
		 * IMPORTANT NOTE: You MUST take all the vertecies in the inanimate and
		 * convert them to Line2D then check to see if their LINE SEGMENTS intersect
		 * (not the lines themselves). This is due to the fact that lines are endless
		 * and will always intersect (unless parallel, which is usually not the case here).
		 * Instead, our inanimates are defined by LINE SEGMENTS and you must check
		 * to see if the segments intersect, not their lines. (NOTE: Line2D has
		 * a method to check intersection of 2 SEGMENTS).
		 *
		 */
		if (v.size() > 2)
			d.inAnimates.add(new inanimateObject(p, d, charge, v));
		else
			d.messages.addMessage("Error: Cannot add inanimate with less than 3 vertecies!", onScreenMessage.CENTER);
		callingFrame.dispatchEvent(new WindowEvent(callingFrame, WindowEvent.WINDOW_CLOSING));
	}
}
