package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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

	public void setXSpeed(double d) {
		this.dx = d;
	}

	public void setYSpeed(double d) {
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
		/*
		 * Represents the THEORETICAL point this ball will move to after this method exits. However, this shall only be used for inanimate
		 * collision check, as the collision with the inaniamates may alter dx and dy, changing the actual point the ball will move to.
		 * Lines:
		 * x = (x+dx*tickLength/1000);
		 * y = (y+dy*tickLength/1000);
		 *
		 * Must not be removed!
		 */
		Point nextPoint = new Point((int)(x+dx*tickLength/1000), (int)(y+dy*tickLength/1000));
		inanimateCollisions(inani, nextPoint);
		x = (x+dx*tickLength/1000);
		y = (y+dy*tickLength/1000);
		inanimateFailSafe(inani);
		this.setColor(Ball.defaultColor); //use Ball., not this. since this is a static variabl -William
		for (inanimateObject o : inani){
			if (o.shape.contains(new Point((int)x, (int)y))){
				this.setColor(Color.red);
			}
		}
	}

	private void inanimateFailSafe(ArrayList<inanimateObject> inani) {
		for (inanimateObject o : inani) {
			//Failsafe - in case balls glitched into the shape and did not collide:
			if (o.shape.contains(getX(), getY())) {
				Line2D closestLine = new Line2D.Float(o.getVerticies().get(o.getVerticies().size()-1), o.getVerticies().get(0));
				double distanceToClosestLine = closestLine.ptSegDist(getX(), getY());
				for(int i = 0; i < o.getVerticies().size() - 1; i++){
					Line2D l = new Line2D.Float(o.getVerticies().get(i), o.getVerticies().get(i+1));
					double d = l.ptSegDist(getX(), getY());
					if(d < distanceToClosestLine) {
						closestLine = l;
						distanceToClosestLine = d;
					}
				}
				double m1 = (closestLine.getY2()-closestLine.getY1())/(closestLine.getX2()-closestLine.getX1());
				double m2 = -1/m1;
				//Line2D normalToLine = new Line2D.Float((float)getX(), (float)getY(), (float)getX()+100, (float)(getY()+100*m2));
				//Line2D xAxis = new Line2D.Float((float)getX(), (float)getY(), (float)getX()+10, (float)getY());
				double degreeBetweenLines = Math.atan(m2);//Degree between normal to inAnimate line and the x axis
				//System.out.println("Setting from: " + getX() + "," + getY() + " To: " + getX()+distanceToClosestLine*Math.cos(degreeBetweenLines) + "," +getY() + distanceToClosestLine*Math.sin(degreeBetweenLines));
				this.setX(getX() + (distanceToClosestLine*Math.cos(degreeBetweenLines)*1.3));
				this.setY(getY() + (distanceToClosestLine*Math.sin(degreeBetweenLines)*1.3));
			}
		}
	}

	private void inanimateCollisions(ArrayList<inanimateObject> inani, Point nextPoint) {
		Point currentLoc = new Point((int)x, (int)y);
		if (!currentLoc.equals(nextPoint)) {
			Line2D hereToNextPoint = new Line2D.Float(currentLoc, nextPoint);
			//Thats a line between current position and the position that we will move to in
			//next frame.

			ArrayList<Line2D> intersectingLines = new ArrayList<Line2D>();

			for (inanimateObject o : inani){
				for (int i = 0; i < o.getVerticies().size(); i++) {
					Line2D l;
					if (i < o.getVerticies().size() - 1)
						l = new Line2D.Float(o.getVerticies().get(i), o.getVerticies().get(i+1));
					else
						l = new Line2D.Float(o.getVerticies().get(i), o.getVerticies().get(0));//Gets line connecting end and start of inanimate

					if (l.intersectsLine(hereToNextPoint)) {
						intersectingLines.add(l);
					}
				}
			}

			double shortestDistance = this.getRadius();
			Line2D closestLine = null;

			for (Line2D l : intersectingLines) {
				if (l.ptSegDist(currentLoc) < shortestDistance) {
					closestLine = l;
				}
			}

			if (closestLine != null) {
				Line2D l = closestLine;
				Line2D h = hereToNextPoint;
				//Move ball close to inanimate before a collision:
				double slopeL = (l.getY1() - l.getY2()) / (l.getX1() - l.getX2());
				double slopeH = (h.getY1() - h.getY2()) / (h.getX1() - h.getX2());
				double yIntL = l.getY1() - (slopeL*l.getX1());
				double yIntH = h.getY1() - (slopeH*h.getX1());
				Point moveTo = new Point((int)((yIntH - yIntL)/(slopeL-slopeH)), (int)(yIntL + (slopeL*((yIntH - yIntL)/(slopeL-slopeH)))));
				//System.out.println(slopeL + " " + slopeH + " " + yIntL + " " + yIntH);
				//System.out.println(this.x + " " + moveTo.x + " " + this.y + " " + moveTo.y);
				if (moveTo.x > 0 && moveTo.x < Integer.MAX_VALUE && moveTo.y > 0 && moveTo.y < Integer.MAX_VALUE) {//Incase either of the slopes is +-Infinity or NaN
					this.x = moveTo.x;
					this.y = moveTo.y;
				}

				double slope = (l.getY1() - l.getY2()) / (l.getX1() - l.getX2());
				double normalSlope = -1/slope;
				// determines whether ball is above or below line
				double ball_pos = y - l.getY1() - slope * (x - l.getX1());
				// dot product to find cosine between vectors
				double dot;
				// since only direction matters, we can assume that the x component is 1
				if (ball_pos > 0)
					dot = dy * normalSlope - dx;
				else
					dot = dx - dy * normalSlope;
				double mag_normal = Math.sqrt(Math.pow(normalSlope, 2) + 1);
				double mag_ball = Math.sqrt(dx*dx + dy*dy);
				// reflected angle
				double angle = Math.PI - Math.acos(dot / (mag_normal * mag_ball));
				double elastic = (double) d.elasticity/(double) 100;
				dx = elastic * mag_ball * Math.cos(angle);
				dy = elastic * mag_ball * Math.sin(angle);
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

		if (x + radius >= width*5/6) {
			if (dx>0) dx = -dx * d.elasticity/100;//If walls are inelastic, and ball is trying to move right.
			hitWall = true;
		}
		
		if (x - radius <= width/6 + 3) {
			if (dx < 0) dx = -dx * d.elasticity/100;//If ball is trying to move left.
			hitWall = true;
		}
		
		if (y + radius >= height*9/10) {
			if (dy > 0) dy = -dy * d.elasticity/100;//If ball is trying to move down.
			hitWall = true;
		}
		
		if (y - radius <= height/6 + 3) {
			if (dy < 0) dy = -dy *d.elasticity/100;//If ball is trying to move up.
			hitWall = true;
		}

		/*
		 * makes sure balls wont escape if they glitch out
		 */
		if (x <= width*5/6) {
			x += radius;
		}
		
		if (x >= width/6 + 3) {
			x -= radius;
		}
		
		if (y >= height * 9/10) {
			y -= radius;
		}
		
		if (y <= height/6 + 3) {
			y += radius;
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
