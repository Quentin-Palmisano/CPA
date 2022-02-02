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
		return tmp1*tmp1 + tmp2*tmp2;
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
				double tmp1 = p1.getX()-p2.getX();
				double tmp2 = p1.getY()-p2.getY();
				double d2 = tmp1*tmp1 + tmp2*tmp2;
				if (d2>d) {
					p=p1;
					q=p2;
					d=d2;
				}
			}
		}

		return new Line(p,q);
	}

	// calculCercleMin: ArrayList<Point> --> Circle
	//   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	
	//EXERCICE 4
	/*public Circle calculCercleMin(ArrayList<Point> points) {
	    if (points.isEmpty()) {
	      return null;
	    }

    Point center=points.get(0);
    int radius=100;

    Line l = calculDiametre(points);
    Point p = l.getP();
    Point q = l.getQ();
    double tmp1 = p.getX()-q.getX();
	double tmp2 = p.getY()-q.getY();
    radius = (int)((Math.sqrt(tmp1*tmp1+tmp2*tmp2))/2);
    
    center = centre(a, b);

    return new Circle(center,radius);
  }*/

	//EXERCICE 5
	/*
	public Circle calculCercleMin(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}
		ArrayList<Point> points_bis = new ArrayList<Point>();
		
		Point center=points.get(0);
		int radius=100;

		Point dummy = points.get((int)Math.random()*points.size());
		Point P = new Point();
		Point Q = new Point();
		double d = 0;
		for(Point p : points) {
			points_bis.add(p);
			double tmp = distance(dummy, p);
			if(tmp>d) {
				P=p;
				d=tmp;
			}
		}
		d=0;
		for(Point p : points) {
			double tmp = distance(P, p);
			if(tmp>d) {
				Q=p;
				d=tmp;
			}
		}
		Point C = centre(P, Q);
		double RAYON = Math.sqrt(distance(P, C));
		Circle CERCLE = new Circle(C,(int)RAYON);
		points_bis.remove(P);
		points_bis.remove(Q);
		while(!points_bis.isEmpty()) {
			Point S = points_bis.get(0);
			double CS = Math.sqrt(distance(C, S));
			if(CS<=RAYON) {
				points_bis.remove(S);
			}else {
				RAYON = (RAYON+CS)/2;
				double ALPHA = RAYON/CS;
				double BETA = 1 - ALPHA;
				double Cx = ALPHA*C.getX()+BETA*S.getX();
				double Cy = ALPHA*C.getY()+BETA*S.getY();
				C = new Point((int)Cx, (int)Cy);
				CERCLE = new Circle(C, (int)RAYON);
				points_bis.remove(S);
			}
		}
		
		return new Circle(C,(int)RAYON);
	}*/
	
	//EXERCICE 6
		public Circle calculCercleMin(ArrayList<Point> points) {
			if (points.isEmpty()) {
				return null;
			}
			ArrayList<Point> points_bis = new ArrayList<Point>();
			
			Point center=points.get(0);
			int radius=100;

			
			for(Point p : points) {
				points_bis.add(p);
			}
			Line l = calculDiametre(points);
			Point P = l.getP();
			Point Q = l.getQ();
			Point C = centre(P, Q);
			double RAYON = Math.sqrt(distance(P, C));
			Circle CERCLE = new Circle(C,(int)RAYON);
			points_bis.remove(P);
			points_bis.remove(Q);
			while(!points_bis.isEmpty()) {
				Point S = points_bis.get(0);
				double CS = Math.sqrt(distance(C, S));
				if(CS<=RAYON) {
					points_bis.remove(S);
				}else {
					RAYON = (RAYON+CS)/2;
					double ALPHA = RAYON/CS;
					double BETA = 1 - ALPHA;
					double Cx = ALPHA*C.getX()+BETA*S.getX();
					double Cy = ALPHA*C.getY()+BETA*S.getY();
					C = new Point((int)Cx, (int)Cy);
					CERCLE = new Circle(C, (int)RAYON);
					points_bis.remove(S);
				}
			}
			
			return new Circle(C,(int)RAYON);
		}
}
