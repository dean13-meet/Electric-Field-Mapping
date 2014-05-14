package Main;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton implements ActionListener, Cloneable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public int timesClicked = 0;
	public int roundLength; //How many times button must be clicked to return to original
	//position.
	ButtonCommands command;
	String[] strs; //Contains the strings that will be displayed on the button
	//with every click.
	public final String name;

	Button(String name, ButtonCommands command, String[] strs, int x, int y, int w, int h) {
		super(strs[0]);
		//System.out.println("Creating button: " + strs[0]);
		this.name = name;

		addActionListener(this);
		this.command = command;
		roundLength = strs.length;
		this.strs = strs;
		this.setBounds(x, y, w, h);
		//System.out.println(this.getBounds());

		this.setFont(new Font("Arial", Font.PLAIN, 16));
		//Make sure text fits in screen:

		if (this.getFontMetrics(this.getFont()).stringWidth(this.getText())>this.getWidth()*0.85) {
			//Get ratio between button width and string width
			double ratio = (double) ((this.getWidth()*0.85) / this.getFontMetrics(this.getFont()).stringWidth(this.getText()));
			//New font size:
			int newSize = (int) (this.getFont().getSize() * ratio);
			//Make sure font is not too high:
			newSize = Math.min(newSize, this.getHeight());

			this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), newSize));
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		this.setText(strs[(timesClicked+1)%roundLength]);
		this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), 16));
		//Make sure text fits in screen:
		if (this.getFontMetrics(this.getFont()).stringWidth(this.getText()) > this.getWidth() * 0.85) {
			//Get ratio between button width and string width
			double ratio = (double) ((this.getWidth() * 0.85) / this.getFontMetrics(this.getFont()).stringWidth(this.getText()));
			//New font size:
			int newSize = (int) (this.getFont().getSize() * ratio);
			//Make sure font is not too high:
			newSize = Math.min(newSize, this.getHeight());

			this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), newSize));
		}
		timesClicked++;
		command.execute(timesClicked);
	}

	public void simulateClick() {
		actionPerformed(new ActionEvent(this, 0, ""));
	}

	public Button getClone() {
		try {
			return (Button) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
