package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import algorithms.Tree2D;

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
				System.out.println("ADD");
				Tree2D tmp5 = new Tree2D(l.getQ(), new ArrayList<>());
				trees[getIndice(points, l.getQ())]= tmp5;
				tree.getSubTrees().add(tmp5);
			}else {
				Tree2D tmp5 = new Tree2D(l.getQ(), new ArrayList<>());
				trees[getIndice(points, l.getQ())]= tmp5;
			}
		}
		return this.Tree;
	}
	
	public Tree2D LineToTree2(ArrayList<Line> aretes) {
		if(aretes.isEmpty()) return new Tree2D(new Point(), new ArrayList<Tree2D>());
		for(Line l : aretes) {
			Tree2D root = trees[getIndice(points, l.getP())];
			Tree2D dest = trees[getIndice(points, l.getQ())];
			if(root==null) {
				Tree2D tmp = null;
				if(dest==null) {
					Tree2D t = new Tree2D(l.getQ(), new ArrayList<>());
					ArrayList<Tree2D> array = new ArrayList<Tree2D>();
					array.add(t);
					tmp = new Tree2D(l.getP(), array);
				}else {
					ArrayList<Tree2D> array = new ArrayList<Tree2D>();
					array.add(dest);
					tmp = new Tree2D(l.getP(), array);
				}
				trees[getIndice(points, l.getP())]= tmp;
			}else {
				Tree2D tmp = null;
				if(dest==null) {
					tmp = new Tree2D(l.getQ(), new ArrayList<>());
					trees[getIndice(points, l.getQ())] = tmp;
					root.getSubTrees().add(tmp);
				}else {
					if(!root.getSubTrees().contains(dest)) root.getSubTrees().add(dest);
				}
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
		System.out.println("NB EDGES : " + edges.size());
		while (edges.size()!=0) {
			current = edges.remove(0);
			if (forest.tag(current.getP())!=forest.tag(current.getQ())) {
				kruskal.add(current);
				forest.reTag(forest.tag(current.getP()),forest.tag(current.getQ()));
			}
		}
		return kruskal;
	}
	
	//Autre kruskal
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
	

	public int[]foret;
	
	public int find(int[]tab, int i) {
		if(tab[i]==-1) {
			return i;
		}else {
			return find(tab, tab[i]);
		}
	}
	
	public void union(int[]tab, int x, int y) {
		x = find(tab, x);
		y = find(tab, y);
		tab[x]=y;
	}
	
	
	//Autre kruskal
	public ArrayList<Line>  kruskal3(ArrayList<Line> aretes) {
		this.foret = new int[points.size()];
		for(int i=0; i<points.size(); i++) {
			this.foret[i]=-1;
		}
		ArrayList<Line> result = new ArrayList<Line>();
		aretes.sort( (Line a, Line b) -> {return (int) (distanceCarre(a.getP(), a.getQ()) - (int) distanceCarre(b.getP(), b.getQ()));} );
		for(Line l : aretes) {
			if(find(this.foret, getIndice(points, l.getP())) == find(this.foret, getIndice(points, l.getQ()))) {
				result.add(l);
				union(this.foret, getIndice(points, l.getP()), getIndice(points, l.getQ()));
			}
		}
		System.out.println("Taille apres krusk : "+ result.size());
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
	
	public void addEdge(HashMap<Integer, ArrayList<Integer>> edges, int i, int j) {
		var list = edges.get(i);
		if(list == null) {
			list = new ArrayList<Integer>();
			edges.put(i, list);
		}
		list.add(j);
	}
	
	
	public HashMap<Integer, ArrayList<Integer>> createGeometricGraph(ArrayList<Point> points, int edgeThreshold) {
		var edges = new HashMap<Integer,ArrayList<Integer>>();
		
		for(int i = 0; i < points.size(); i++) {
			for(int j = 0; j < points.size(); j++) {
				if (points.get(i).distance(points.get(j)) < edgeThreshold) {
					addEdge(edges, i, j);
					addEdge(edges, j, i);
				}
			}
		}
		
		return edges;
	}
	
	class DijkstraData {
		public DijkstraData(int point, double distance) {
			this.point = point;
			this.distance = distance;
		}
		int point; // le point actuel pour la priority queue, le point précédent dans la hashmap
		double distance; // distance au point de départ
	}
	
	
	public double shortestPathToTree(ArrayList<Point> points, HashMap<Integer, ArrayList<Integer>> edges, HashSet<Integer> treePoints, int start, ArrayList<Integer> path) {
		
		var pq = new PriorityQueue<DijkstraData>((a, b) -> {return a.distance < b.distance ? -1 : 1;});
		var map = new HashMap<Integer, DijkstraData>();
		
		pq.add(new DijkstraData(start, 0));
		map.put(start, new DijkstraData(-1, 0));
		
		while(!pq.isEmpty()) {
			var u = pq.poll();
			
			if(treePoints.contains(u.point)) {
				int p = u.point;
				while(p != -1) {
					path.add(p);
					p = map.get(p).point;
				}
				return u.distance;
			}
			
			var adjacents = edges.get(u.point);
			for(int adjacent : adjacents) {
				double distance = u.distance + points.get(u.point).distance(points.get(adjacent));
				
				var v = map.get(adjacent);
				if(v == null) {
					v = new DijkstraData(u.point, distance);
					map.put(adjacent, v);
				} else if(v.distance > distance) {
					v.distance = distance;
				} else {
					continue;
				}
				
				pq.add(new DijkstraData(adjacent, v.distance));
				
			}
			
		}
		
		return -1;
		
	}
	
	public Tree2D findPointInTree(Tree2D tree, Point point) {
		
		if(tree.getRoot().equals(point)) {
			return tree;
		}
		
		for(Tree2D subTree : tree.getSubTrees()) {
			var ret = findPointInTree(subTree, point);
			if(ret != null) return ret;
		}
		
		return null;
		
	}
	
	public Tree2D addPathToTree(Tree2D tree, ArrayList<Point> points, ArrayList<Integer> path) {
		var pathTree = findPointInTree(tree, points.get(path.get(0)));
		
		for(int i = 1; i<path.size(); i++) {
			int p = path.get(i);
			var ntree = new Tree2D(points.get(p), new ArrayList<>());
			pathTree.getSubTrees().add(ntree);
			pathTree = ntree;
		}
		
		return tree;
		
	}
	
	public int findPoint(ArrayList<Point> points, Point point) {
		for(int i = 0; i<points.size(); i++) {
			var p = points.get(i);
			if(p.equals(point)) {
				return i;
			}
		}
		return -1;
	}
	
	
	public Tree2D calculSteinerDijktra(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		var edges = createGeometricGraph(points, edgeThreshold);
		
		var treePoints = new HashSet<Integer>();
		
		var start = hitPoints.get(0);
		int startIndex = findPoint(points, start);
		
		Tree2D tree = new Tree2D(start, new ArrayList<>());
		treePoints.add(startIndex);
		
		for(int h = 1; h<hitPoints.size(); h++) {
			double minDistance = 0;
			ArrayList<Integer> bestPath = new ArrayList<>();
			for(int i = 0; i<hitPoints.size(); i++) {
				var hitPoint = findPoint(points, hitPoints.get(i));
				if(treePoints.contains(hitPoint)) continue;
				var path = new ArrayList<Integer>();
				double distance = shortestPathToTree(points, edges, treePoints, hitPoint, path);
				//System.out.println("point " + i + " distance " + distance + " pathsize " + path.size());
				if((minDistance == 0.0 || distance < minDistance) && (distance != -1 && path.size() > 1)) {
					minDistance = distance;
					bestPath = path;
				}
			}
			
			if(minDistance == 0.0) return tree;
			
			//System.out.println("adding point " + (h+1) + " distance " + minDistance + " pathsize " + bestPath.size());
			
			tree = addPathToTree(tree, points, bestPath);

			for(int p : bestPath) {
				treePoints.add(p);
			}
		}
		
		return tree;
	}
	
	

	/*

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

		System.out.println("NB ARETES : " + aretes.size());
		//aretes = kruskal3(aretes);
		System.out.println("NB ARETES : " + aretes.size());
		LineToTree(aretes);
		
		return this.Tree;
	}
	*/
	
	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		return calculSteinerDijktra(points, edgeThreshold, hitPoints);
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
