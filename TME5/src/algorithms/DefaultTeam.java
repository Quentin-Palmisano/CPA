package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Line;


public class DefaultTeam {

	public double distance(Point a, Point b) {
		double tmp1 = a.getX()-b.getX();
		double tmp2 = a.getY()-b.getY();
		return Math.sqrt(tmp1*tmp1 + tmp2*tmp2);
	}
	public double distance(Line l) {
		double tmp1 = l.getP().getX()-l.getQ().getX();
		double tmp2 = l.getP().getX()-l.getQ().getY();
		return Math.sqrt(tmp1*tmp1 + tmp2*tmp2);
	}
	
	public boolean contain(Point a, Point b, ArrayList<Line> line) {
		for(Line l : line) {
			int px = l.getP().x;
			int py = l.getP().y;
			int qx = l.getQ().x;
			int qy = l.getQ().y;
			if((a.x==px && a.y==py && b.x==qx && b.y==qy)||(a.x==qx && a.y==qy && b.x==px && b.y==py)) return true;
		}
		return false;
	}
	
	public void iter(int[][] paths, double [][] dist, int a, int b) {
		int i = -1;
		while(i!=b) {
			
		}
	}

	public int[][] calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> copy = new ArrayList<>(points);
		ArrayList<Line> aretes = new ArrayList<Line>();
		for(Point p1 : points) {
			for(Point p2 : copy) {
				Line tmp = new Line(p1, p2);
				if((int)distance(tmp)<edgeThreshold) aretes.add(tmp);
	
			}
			copy.remove(p1);
		}

		int[][] paths=new int[points.size()][points.size()];
		double[][] dist=new double[points.size()][points.size()];

		for (int i=0;i<paths.length;i++) {
			for (int j=0;j<paths.length;j++) {
				Point a = points.get(i);
				Point b = points.get(j);
				if(contain(a, b, aretes)) {
					paths[i][j]=j;
					dist[i][j]=distance(a,b);
				}else {
					paths[i][j]=-1;
					dist[i][j]=Integer.MAX_VALUE;
					iter(paths, dist, i, j);
				}
			}
		}

		/*for (int i=0;i<paths.length;i++) {
			for (int j=0;j<paths.length;j++) {
				paths[i][j]=(i+1)%paths.length;
			}
		}*/

		return new int[points.size()][points.size()];
	}
	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		//REMOVE >>>>>
		Tree2D leafX = new Tree2D(new Point(700,400),new ArrayList<Tree2D>());
		Tree2D leafY = new Tree2D(new Point(700,500),new ArrayList<Tree2D>());
		Tree2D leafZ = new Tree2D(new Point(800,450),new ArrayList<Tree2D>());
		ArrayList<Tree2D> subTrees = new ArrayList<Tree2D>();
		subTrees.add(leafX);
		subTrees.add(leafY);
		subTrees.add(leafZ);
		Tree2D steinerTree = new Tree2D(new Point(750,450),subTrees);
		//<<<<< REMOVE

		return new Tree2D(null,null);
	}
}
