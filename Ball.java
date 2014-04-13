import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;


public class Ball {
	public double x, y, mySize, mass, dx, dy, charge, acceleration, accelerationD;
	public boolean hitWall;
	public initialDisplay d;
	private Color color = Color.GREEN;
	public static final Color defualtColor = Color.green;
	
	public Force force = new Force();
	Line2D.Double forceVector;


	public Ball (initialDisplay d, double mass, double X, double Y, double dx, double dy, double charge) {
		this.d = d;
		mySize = Math.pow(400000*mass, 0.5)*2;
		x = X;
		y = Y;
		dx = dx;
		dy = dy;
		this.mass = mass;
		
		//double angle = 2 * Math.PI * Math.random();  // Random direction.
		
		hitWall = false;
		this.charge = charge;
	}

	public double getRadius() {
		return mySize/2;
	}
	

	public double getXSpeed(){
		return dx;
	}
	public double getYSpeed(){
		return dy;
	}

	public void draw(Graphics g) {
		double xx = x-getRadius();
		double yy = y-getRadius();
		g.setColor(color);
		g.fillOval((int) xx, (int) yy, (int) mySize, (int) mySize);
	}

	public void update(Graphics g, int width, int height, int tickLength) {
		wallcollisions(width, height);
		updateAcceleration();
		updateForceVector();
		//g.drawLine((int)forceVector.x1, (int)forceVector.y1, (int)forceVector.x2, (int)forceVector.y2);
		dx+=Math.cos(accelerationD)*acceleration*tickLength/1000;
		dy+=Math.sin(accelerationD)*acceleration*tickLength/1000;
		x = x+dx*tickLength/1000;
		y = y+dy*tickLength/1000;
	} 
	
	public void updateAcceleration() {
		acceleration = force.magnitude/mass;
		accelerationD = force.direction;
	}
	public void updateForceVector(){
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
			if(d.elasticWalls)dx*=-1;else if(dx>0)dx = 0;//If walls are inelastic, and ball is trying
														 //to move right.
			hitWall = true;
		}
		if (x-radius <= width/6 + 3) {
			if(d.elasticWalls)dx*=-1;else if(dx<0)dx=0;//If ball is trying to move left.
			hitWall = true;
		}
		if (y+radius >= height*9/10) {
			if(d.elasticWalls)dy*=-1;else if(dy>0)dy=0;//If ball is trying to move down.
			hitWall = true;
		}
		if (y-radius <= height/6 + 3) {
			if(d.elasticWalls)dy*=-1;else if(dy<0)dy=0;//If ball is trying to move up.
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
	public void setY(double y){
		this.y = y;
	}
	public double getX(){
		return x;
	}
	public void setX(double x){
		this.x = x;
	}
	public Color getColor(){
		return color;
	}
	public void setColor(Color c){
		this.color = c;
	}
	public void setMass(double m){
		this.mass = m;
		setSize(Math.pow(400000*m, 0.5)*2);//Same as the calculation in the constructor
	}
	private void setSize(double d){
		this.mySize = d;
	}

}


