public class Force {
	public double magnitude;
	public double direction;

	//direction in radians going counter clockwise starting at 0 degrees (positive x axis)
	Force(double magnitude, double direction) {
		this.magnitude = magnitude;
		this.direction = direction;
	}
	Force() {
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

	// TODO: move to separate physics class
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
}
