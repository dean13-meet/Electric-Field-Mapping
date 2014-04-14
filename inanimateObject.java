import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public class inanimateObject {
	private final Program hostP;
	private final initialDisplay d;

	private double charge;
	private Polygon shape;
	//private final ArrayList<Point> vertices;
	/*
	 * Note: Vertices MUST be in circular order! E.g. for the following:
	 *
	 * 		A-----B
	 * 		|     |
	 * 		|	  |
	 * 		C-----D
	 *
	 * That must be represented as: ABDC or ACDB, etc.. Cannot give vertices in order: ABCD, or any
	 * other order for that matter.
	 *
	 */
	private final Point centroid;

	public inanimateObject(Program p, initialDisplay d, double charge, ArrayList<Point> vertices){
		this.hostP = p;
		this.d = d;
		this.charge = charge;
		this.shape = shapeFromVertices(vertices);
		//this.vertices = vertices;
		this.centroid = calcCentroid();
	}

	private Polygon shapeFromVertices(ArrayList<Point> vertices) {
		Polygon shape = new Polygon();
		for (int i = 0; i < vertices.size(); i++) {
			shape.addPoint(vertices.get(i).x, vertices.get(i).y);
		}
		return shape;
	}

	private Point calcCentroid() {
		/*
		 * The centroid in equaly distributed polygons (equal density everywhere) is also
		 * the center of mass, and the average location of charge.
		 * Therefore, it may be used to represent the ENTIRE location of the figure for the purpose
		 * of Vector, Force, and Electric Force calculations.
		 *
		 * Centroid calculations based of these formulas:
		 * http://en.wikipedia.org/wiki/Centroid#Centroid_of_polygon
		 */

		//First, we must get the area of the polygon:
		double areaSigma = 0;
		for(int i = 0; i < shape.npoints; i++) {
			Point current = new Point(shape.xpoints[i], shape.ypoints[i]);
			Point next;
			if (i+1 >= shape.npoints){next = new Point(shape.xpoints[0], shape.ypoints[0]);}
			else {next = new Point(shape.xpoints[i+1], shape.ypoints[i+1]);}
			areaSigma += (current.x*next.y - next.x*current.y);
		}
		double finalArea = areaSigma/2;

		//Then, we get each of the coordinates of the centroid:
		//x:
		double xCoordSigma = 0;
		for(int i = 0; i < shape.npoints; i++) {
			Point current =  new Point(shape.xpoints[i], shape.ypoints[i]);
			Point next;
			if (i+1 >= shape.npoints){next = new Point(shape.xpoints[0], shape.ypoints[0]);}
			else {next = new Point(shape.xpoints[i+1], shape.ypoints[i+1]);}
			xCoordSigma += ((current.x + next.x)*(current.x*next.y - next.x*current.y));
		}
		double finalXCoord = (1/(6*finalArea))*xCoordSigma;

		//y:
		double yCoordSigma = 0;
		for(int i = 0; i < shape.npoints; i++) {
			Point current =  new Point(shape.xpoints[i], shape.ypoints[i]);
			Point next;
			if (i+1 >= shape.npoints){next = new Point(shape.xpoints[0], shape.ypoints[0]);}
			else {next = new Point(shape.xpoints[i+1], shape.ypoints[i+1]);}
			yCoordSigma += ((current.y+next.y)*(current.x*next.y-next.x*current.y));
		}
		double finalYCoord = (1/(6*finalArea))*yCoordSigma;

		return new Point((int)finalXCoord, (int)finalYCoord);
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double d) {
		this.charge = d;
	}

	public Point getCentroid() {
		return this.centroid;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(255, 111, 0));
		g.fillPolygon(shape);
		g.setColor(Color.cyan);
		g.fillOval(centroid.x, centroid.y, 5, 5);
	}
}
