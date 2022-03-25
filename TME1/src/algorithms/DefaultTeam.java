package algorithms;

import java.awt.Point;
import java.util.ArrayList;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {
	
	public double distance(Point a, Point b) {
		double tmp1 = a.getX()-b.getX();
		double tmp2 = a.getY()-b.getY();
		return Math.sqrt(tmp1*tmp1 + tmp2*tmp2);
	}
	
	public Point centre(Point a, Point b){
		int x = (int)((a.getX()+b.getX())/2);
	    int y = (int)((a.getY()+b.getY())/2);
		return new Point(x, y);
	}

	// calculDiametre: ArrayList<Point> --> Line
	//   renvoie une paire de points de la liste, de distance maximum.
	
	public Line calculDiametre(ArrayList<Point> points) {
		if (points.size()<3) {
			return null;
		}

		Point p=points.get(0);
		Point q=points.get(1);

		double d = 0;
		for(Point p1 : points) {
			for(Point p2 : points) {
				double d2 = distance(p1, p2);
				if (d2>d) {
					p=p1;
					q=p2;
					d=d2;
				}
			}
		}

		return new Line(p,q);
	}
	
	public Circle calculCercleMin(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}

		return WelzlCircle.minCircle(points);
	}
}
