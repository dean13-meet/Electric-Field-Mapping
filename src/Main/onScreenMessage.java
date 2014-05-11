package src.Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class onScreenMessage {
	private final Program p;
	private Queue<String> messages = new LinkedList<String>();
	private Queue<Point> locations = new LinkedList<Point>();
	//Must be private so they can only be used in addMessage and printMessage (below)
	//This ensures that they are always synchronized with one another.

	public static Point CENTER = new Point(-1000,-1000);//This just calls the getCenterMethod
	public final Font f = new Font("Gigi", Font.BOLD, 25);

	public onScreenMessage(Program p) {
		this.p = p;
	}

	public void addMessage(String str, Point loc) {
		messages.add(str);
		if(loc.equals(CENTER))loc = getCenter(str, (int)p.DISPLAY_WIDTH, (int)p.DISPLAY_HEIGHT);
		locations.add(loc);
	}

	public void clearMessages() {
		messages.clear();
		locations.clear();
	}

	public void printMessage() {
		Graphics g = p.initialF.getGraphics();
		g.setFont(f);
		g.setColor(Color.black);
		g.fillRect(locations.peek().x, (int)(locations.peek().y-g.getFontMetrics(f).getHeight() * 0.8),
				g.getFontMetrics(f).stringWidth(messages.peek()), (int)(g.getFontMetrics(f).getHeight() * 1.2));//*1.2 is just so that we have a small border around.
		g.setColor(Color.white);
		g.drawString(messages.poll(), locations.peek().x, locations.poll().y);
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	public Point getCenter(String str, int width, int height) {
		Graphics g = p.initialF.getGraphics();
		return new Point((width-g.getFontMetrics(f).stringWidth(str))/2, (height - g.getFontMetrics(f).getHeight())/2);
	}
}
