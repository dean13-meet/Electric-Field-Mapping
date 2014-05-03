package Utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class ArrowHead {
	
	private final int angleA = 60;//Half angle between 2 arrow lines
	private int angle; //Angle arrow is facing
	private int length;
	private Point loc;
	
	
	public ArrowHead(int angle, int length, Point loc){
		this.angle = angle;
		this.length = length;
		this.loc = loc;
	}

	public void draw(Graphics g){
		g.drawLine(loc.x, loc.y, (int)(loc.x+length*Math.cos(Math.toRadians(angle+90+angleA))), (int)(loc.y+length*Math.sin(Math.toRadians(angle+90+angleA))));
		g.drawLine(loc.x, loc.y, (int)(loc.x+length*Math.cos(Math.toRadians(angle-90-angleA))), (int)(loc.y+length*Math.sin(Math.toRadians(angle-90-angleA))));
	}
}
