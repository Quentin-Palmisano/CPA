package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.*;

public class DefaultTeam {
	
	//Tree2D racine de tout les autres Tree2D
	public Tree2D Tree;
	//Liste de tout les points d'origine
	public ArrayList<Point> points;
	//tableau des Tree2D dont l'indice correspond a l'indice du point racine dans la liste points
	public Tree2D[] trees;

	//Classe fournie par le prof pour l'aldo de kruskal
	class NameTag {
		private ArrayList<Point> points;
		private int[] tag;
		protected NameTag(ArrayList<Point> points){
			this.points=(ArrayList<Point>)points.clone();
			tag=new int[points.size()];
			for (int i=0;i<points.size();i++) tag[i]=i;
		}
		protected void reTag(int j, int k){
			for (int i=0;i<tag.length;i++) {
				if (tag[i]==j) tag[i]=k;
			}
		}
		protected int tag(Point p){
			for (int i=0;i<points.size();i++) {
				if (p.equals(points.get(i))) return tag[i];
			}
			return 0xBADC0DE;
		}
	}
	
	//Algo fourni par le prof pour le clalcul de tout les plus court chemin entre chaque point
	public int[][] calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {

		int[][] paths=new int[points.size()][points.size()];
		double[][] dist=new double[points.size()][points.size()];

		for (int i=0;i<paths.length;i++) { 
			for (int j=0;j<paths.length;j++) {
				paths[i][j]=i;
			}
		}


		for (int i=0;i<paths.length;i++) {
			for (int j=0;j<paths.length;j++) {
				if (i==j) {dist[i][i]=0; continue;}
				if (points.get(i).distance(points.get(j))<=edgeThreshold) dist[i][j]=points.get(i).distance(points.get(j)); else dist[i][j]=Double.POSITIVE_INFINITY;
				paths[i][j]=j;
			}
		}
		for (int k=0;k<paths.length;k++) {
			for (int i=0;i<paths.length;i++) {
				for (int j=0;j<paths.length;j++) {
					if (dist[i][j]>dist[i][k] + dist[k][j]){
						dist[i][j]=dist[i][k] + dist[k][j];
						paths[i][j]=paths[i][k];

					}
				}
			}
		}
		return paths;
	}
	
	//Verifie si l'aretes de Point respectif p et q est dans l'ensemble des aretes de lines
	public boolean contain(ArrayList<Line> lines, Point p, Point q) {
		for(Line l : lines) {
			if((l.getP()==p && l.getQ()==q)||(l.getP()==q && l.getQ()==p)) return true;
		}
		return false;
	}
	
	//Distance au carre
	public double distanceCarre(Point a, Point b) {
		double tmp1 = a.getX()-b.getX();
		double tmp2 = a.getY()-b.getY();
		return tmp1*tmp1 + tmp2*tmp2;
	}	
	
	//Renvoie l'indice auquel se trouve le point p dans la liste points, -1 s'il n'existe pas
	public int getIndice(ArrayList<Point> points, Point p) {
		for(int i=0; i<points.size(); i++) {
			Point q = points.get(i);
			if(p.x==q.x && p.y==q.y) return i;
		}
		return -1;
	}
	
	//Renvoie le Tree2D qui a pour racine le point p
	//NE FONCTIONNE PAS !!!
	//on a un tableau trees en variable global qui gère ca
	public Tree2D getTree(Tree2D tree, Point p) {
		if(getIndice(points,p)==getIndice(points,tree.getRoot())) {
			return tree;
		}else {
			ArrayList<Tree2D> subtree = tree.getSubTrees();
			for(Tree2D t : subtree) {
				return getTree(t, p);
			}
		}
		return null;
	}
	
	//Transforme une liste d'aretes en Tree2D
	public Tree2D LineToTree(ArrayList<Line> aretes) {
		if(aretes.isEmpty()) return new Tree2D(new Point(), new ArrayList<Tree2D>());
		for(Line l : aretes) {
			Tree2D tree = trees[getIndice(points, l.getP())];
			if(tree!=null) {
				Tree2D tmp5 = new Tree2D(l.getQ(), new ArrayList<>());
				trees[getIndice(points, l.getQ())]= tmp5;
				tree.getSubTrees().add(tmp5);
			}
		}
		return this.Tree;
	}
	
	//Renvoie la liste de tout les points appartenant a une liste d'aretes
	public ArrayList<Point> getPointsList(ArrayList<Line> edges){
		ArrayList<Point> result = new ArrayList<Point>();
		for(Line l : edges) {
			if(!result.contains(l.getP())) result.add(l.getP());
			if(!result.contains(l.getQ())) result.add(l.getQ());
		}
		return result;
	}
	
	//Kruskal du prof
	public ArrayList<Line> kruskal1(ArrayList<Line> edges) {
		edges.sort( (Line a, Line b) -> {return (int) (distanceCarre(a.getP(), a.getQ()) - (int) distanceCarre(b.getP(), b.getQ()));} );
		ArrayList<Line> kruskal = new ArrayList<Line>();
		Line current;
		NameTag forest = new NameTag(getPointsList(edges));
		while (edges.size()!=0) {
			current = edges.remove(0);
			if (forest.tag(current.getP())!=forest.tag(current.getQ())) {
				kruskal.add(current);
				forest.reTag(forest.tag(current.getP()),forest.tag(current.getQ()));
			}
		}
		return kruskal;
	}
	
	public ArrayList<Line>  kruskal2(ArrayList<Line> aretes) {
		ArrayList<Line> result = new ArrayList<Line>();
		int i, n, num1, num2;
		Line x;
		n = getPointsList(aretes).size();
		int[] CC = new int[points.size()];
		for (i = 0; i < n; i++)
			CC[getIndice(points, points.get(i))]=i;
		
		aretes.sort( (Line a, Line b) -> {return (int) (distanceCarre(a.getP(), a.getQ()) - (int) distanceCarre(b.getP(), b.getQ()));} );
		
		i = 0;
		while (result.size() < n - 1 && i<aretes.size()) {
			x = aretes.get(i);
			num1 = CC[getIndice(points, x.getP())];
			num2 = CC[getIndice(points, x.getQ())];
			if (num1 != num2) {
				result.add(x);
				for (Point s : points)
					if (CC[getIndice(points, s)] == num2) CC[getIndice(points, s)] = num1;
			}
			i++;
		}
		return result;
	}

	//Transforme une liste de points en Tree2D
	public Tree2D PointToTree(ArrayList<Point> points) {
		if(points.isEmpty()) return new Tree2D(new Point(), new ArrayList<Tree2D>());
		Point head = points.remove(0);
		if(points.isEmpty()) {
			return new Tree2D(head, new ArrayList<Tree2D>());
		}else {
			ArrayList<Tree2D> tree = new ArrayList<Tree2D>();
			tree.add(PointToTree(points));
			return new Tree2D(head, tree);
		}
	}
	
	
	
	
	
	


	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		
		this.points=points;
		this.trees = new Tree2D[points.size()];
		int[][] paths = calculShortestPaths(points, edgeThreshold);
		
		ArrayList<Line> aretes = new ArrayList<Line>();
		for(int i=0; i<hitPoints.size()-1; i++) {
			Point P = hitPoints.get(i);
			Point Q = hitPoints.get(i+1);
			int p = getIndice(points, P);
			int q = getIndice(points, Q);
			while (p!=q) {
				aretes.add(new Line(points.get(p),points.get(paths[p][q])));
				p = paths[p][q];
			}
		};
		
		Line head = aretes.get(0);
		Tree2D tmp1 = new Tree2D(head.getQ(), new ArrayList<>());
		trees[getIndice(points, head.getQ())]= tmp1;
		
		ArrayList<Tree2D> tmp2 = new ArrayList<Tree2D>();
		tmp2.add(tmp1);
		this.Tree = new Tree2D(head.getP(), tmp2);
		trees[getIndice(points, head.getP())]= this.Tree;

		//kruskal2(aretes);
		LineToTree(aretes);
		
		return this.Tree;
	}

	public Tree2D calculSteinerBudget(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		
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

		return steinerTree;
	}
}
