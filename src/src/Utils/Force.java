package src.Utils;
import src.Main.Ball;

public class Force {
	public double magnitude;
	public double direction;
	public final static double permitivity_of_free_space = 8.85418782 * Math.pow(10, -12);
	public final static double k = 8.987551787368176*Math.pow(10, 9);

	//direction in radians going counter clockwise starting at 0 degrees (positive x axis)
	public Force(double magnitude, double direction) {
		this.magnitude = magnitude;
		this.direction = direction;
	}
	public Force() {
		this(0,0);
	}
	public String toString() {
		return "( " + magnitude + " , " + direction + " )";
	}

	public double getX() {
		return Math.cos(direction)*magnitude;
	}
	public double getY() {
		return Math.sin(direction)*magnitude;
	}

	public void add(Force f) {
		double myX = this.getX();
		double hisX = f.getX();
		double myY = this.getY();
		double hisY = f.getY();

		double newX = myX + hisX;
		double newY = myY + hisY;
		this.magnitude = Math.pow(Math.pow(newX, 2) + Math.pow(newY, 2), 0.5);

		if(newX == 0) {
			if(newY > 0) {
				direction = Math.PI/2;
				return;
			}
			if(newY < 0) {
				direction = 3*Math.PI/2;
				return;
			}
		}

		if(newY == 0) {
			if(newX > 0) {
				direction = 0;
				return;
			}
			if(newX < 0) {
				direction = Math.PI;
				return;
			}
		}

		if(newX > 0) {
			direction = Math.atan(newY/newX);
		}
		if (newX < 0) {
			direction = Math.atan(newY/newX) +Math.PI;
		}
	}
	public static double distance(double x, double y, double x2, double y2) {
		return Math.pow(Math.pow(x - x2, 2) + Math.pow(y - y2, 2), 0.5);
	}
	/**
	 * Calculates the potential energy between two balls.
	 * @param ball
	 * @param ball2
	 * @return
	 */
	public static double CalculatePotentialEnergy(Ball ball, Ball ball2) {
		return Force.k*ball.charge*ball2.charge/Force.distance(ball.getX(), ball2.getX(), ball.getY(), ball2.getY());
	}
}
