package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import Utils.Force;


public class Ball {
	private double x, y;
	public double mySize, mass, dx, dy, charge, acceleration, accelerationD;
	public boolean hitWall;
	public initialDisplay d;
	private Color color = Color.GREEN;
	public static final Color defaultColor = Color.green;

	public Force force = new Force();
	Line2D.Double forceVector;

	public Ball (initialDisplay d, double mass, int X, int Y, double dx, double dy, double charge) {
		this.d = d;
		mySize = Math.pow(400000*mass, 0.5)*2;
		x = X;
		y = Y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;

		//double angle = 2 * Math.PI * Math.random();  // Random direction.

		hitWall = false;
		this.charge = charge;
	}

	public double getRadius() {
		return mySize/2;
	}


	public double getXSpeed() {
		return dx;
	}
	public void setXSpeed(double d){
		this.dx = d;
	}
	public void setYSpeed(double d){
		this.dy = d;
	}
	public double getYSpeed() {
		return dy;
	}

	public void draw(Graphics g) {
		double xx = x-getRadius();
		double yy = y-getRadius();
		g.setColor(color);
		g.fillOval((int) xx, (int) yy, (int) mySize, (int) mySize);
	}

	public void update(Graphics g, int width, int height, int tickLength, ArrayList<inanimateObject> inani) {
		wallcollisions(width, height);
		updateAcceleration();
		updateForceVector();
		//g.drawLine((int)forceVector.x1, (int)forceVector.y1, (int)forceVector.x2, (int)forceVector.y2);
		dx+=Math.cos(accelerationD)*acceleration*tickLength/1000;
		dy+=Math.sin(accelerationD)*acceleration*tickLength/1000;
		inanimateCollisions(inani);
		x = (x+dx*tickLength/1000);
		y = (y+dy*tickLength/1000);
	}

	private void inanimateCollisions(ArrayList<inanimateObject> inani) {
		for(inanimateObject o : inani){
			for(int i = 0; i < o.getVertecies().size(); i++){
				Line2D l;
				if(i<o.getVertecies().size()-1)l = new Line2D.Float(o.getVertecies().get(i), o.getVertecies().get(i+1));
				else l = new Line2D.Float(o.getVertecies().get(i), o.getVertecies().get(0));//Gets line connecting end and start of inanimate

				if(l.ptSegDist(getX(), getY())<=1){//If the ball is less than or equal to 1 pixels from the line
					dx = 0; 
					dy = 0;
				}
			}
		}
		
	}

	public void updateAcceleration() {
		acceleration = force.magnitude/mass;
		accelerationD = force.direction;
	}
	public void updateForceVector() {
		forceVector = new Line2D.Double(x, y, x +
				Math.cos(force.direction)*force.magnitude,
				y + Math.sin(force.direction)*force.magnitude);
	}

	public void wallcollisions(int width, int height) {
		hitWall = false;
		int radius = (int) getRadius();
		/*
		 * checks collisions with walls
		 */

		if (x+radius >= width*5/6) {
			if(dx>0)dx = -dx *d.elasticity/100;//If walls are inelastic, and ball is trying to move right.
			hitWall = true;
		}
		if (x-radius <= width/6 + 3) {
			if(dx<0)dx = -dx * d.elasticity/100;//If ball is trying to move left.
			hitWall = true;
		}
		if (y+radius >= height*9/10) {
			if(dy>0)dy = -dy * d.elasticity/100;//If ball is trying to move down.
			hitWall = true;
		}
		if (y-radius <= height/6 + 3) {
			if(dy<0)dy = -dy *d.elasticity/100;//If ball is trying to move up.
			hitWall = true;
		}

		/*
		 * makes sure balls wont escape if they glitch out
		 */
		if (x <= width*5/6) {
			x+=radius;
		}
		if (x >= width/6 + 3) {
			x-=radius;
		}
		if(y >= height*9/10) {
			y-=radius;
		}
		if(y <= height/6 + 3) {
			y+=radius;
		}
	}

	public double getSpeed() {
		return Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5);
	}

	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double e) {
		this.x = e;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color c) {
		this.color = c;
	}
	public double getMass(){
		return mass;
	}
	public double getCharge(){
		return charge;
	}
	public void setCharge(double d){
		this.charge = d;
	}
	public void setMass(double m){
		this.mass = m;
		setSize(Math.pow(400000*m, 0.5)*2);//Same as the calculation in the constructor
	}
	private void setSize(double d){
		this.mySize = d;
	}
	public String toString() {
		return mass + " " + x + " " + y + " " + dx + " " + dy + " " + charge;
	}

	public double getDirection() {
		if (dx < 0)
			return Math.atan(dy/dx) + Math.PI;
		if (dx > 0)
			return Math.atan(dy/dx);
		if (dy > 0)
			return Math.PI/2;
		if (dy < 0) 
			return -Math.PI/2;
		return 0;
	}

}
