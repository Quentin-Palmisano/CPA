package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import supportGUI.Line;


public class DefaultTeam {

	public double distanceCarre(Point a, Point b) {
		double tmp1 = a.getX()-b.getX();
		double tmp2 = a.getY()-b.getY();
		return tmp1*tmp1 + tmp2*tmp2;
	}

	public Point plusProche(Point point, ArrayList<Point> points) {
		if(points.isEmpty()) return null;
		Point pMax = points.get(0);
		double distMax = Integer.MAX_VALUE;
		for(Point p : points) {
			if(p!=point) {
				double dist = distanceCarre(point, p);
				if(dist<distMax) {
					pMax = p;
					distMax = dist;
				}
			}
		}
		points.remove(pMax);
		return pMax;
	}

	public Tree2D calculSteinerNaif(ArrayList<Point> points) {

		ArrayList<Point> copy = new ArrayList<>(points);

		int r = new Random().nextInt(copy.size());
		Point rand = copy.get(r);
		Point courant = plusProche(rand, copy);
		copy.remove(r);

		ArrayList<Tree2D> tmp = new ArrayList<Tree2D>();
		tmp.add(new Tree2D(courant, new ArrayList<Tree2D>()));

		Tree2D myResult = new Tree2D(rand , tmp);

		while(!copy.isEmpty()) {
			courant = plusProche(courant, copy);
			tmp = tmp.get(0).getSubTrees();
			tmp.add(new Tree2D(courant, new ArrayList<Tree2D>()));
		}
		
		return myResult;
	}
	
	public Tree2D calculSteinerKruskal(ArrayList<Point> points) {

		ArrayList<Point> copy = new ArrayList<>(points);
		ArrayList<Line> aretes = new ArrayList<>();
		HashMap<Point, String> alreadyIn = new HashMap<>();
		
		for(Point p1 : copy) {
			for(Point p2 : copy) {
				aretes.add(new Line(p1, p2));
			}
			copy.remove(p1);
		}
		
		aretes.sort( (Line a, Line b) -> {return (int) (distanceCarre(a.getP(), a.getQ()) - (int) distanceCarre(b.getP(), b.getQ()));} );
		
		Point root = aretes.get(0).getP();
		Point courant = aretes.get(0).getQ();
		alreadyIn.put(root, "");
		alreadyIn.put(courant, "");
		aretes.remove(0);
		
		ArrayList<Tree2D> tmp = new ArrayList<Tree2D>();
		tmp.add(new Tree2D(courant, new ArrayList<Tree2D>()));

		Tree2D myResult = new Tree2D(root , tmp);
		
		for(Line l : aretes) {
			
		}
		

		while(!copy.isEmpty()) {
			courant = plusProche(courant, copy);
			tmp = tmp.get(0).getSubTrees();
			tmp.add(new Tree2D(courant, new ArrayList<Tree2D>()));
		}
		
		return myResult;
	}



	public Tree2D calculSteiner(ArrayList<Point> points) {
		/*
		Tree2D leafX = new Tree2D(new Point(700,400),new ArrayList<Tree2D>());
		Tree2D leafY = new Tree2D(new Point(700,500),new ArrayList<Tree2D>());
		Tree2D leafZ = new Tree2D(new Point(800,450),new ArrayList<Tree2D>());

		ArrayList<Tree2D> subTrees = new ArrayList<Tree2D>();
		subTrees.add(leafX);
		subTrees.add(leafY);
		subTrees.add(leafZ);
		Tree2D myResult = new Tree2D(new Point(750,450),subTrees);

		return myResult;
		 */
		return calculSteinerNaif(points);
	}
}
