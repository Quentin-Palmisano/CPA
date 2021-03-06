package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import algorithms.Tree2D;

import supportGUI.*;

public class DefaultTeam {

	//----------------------------------------
	//----------------------------------------
	//------------VERSION DU COURS------------
	//----------------------------------------
	//----------------------------------------
	
	//Cette version est donc la version original
	//qui ne fonctionne pas mais que nous avons 
	//decider de vous laisser.
	//Il s'agit de toute la partie en commentaire.
	
	
	

	/*

	//Tree2D racine de tout les autres Tree2D
	public Tree2D Tree;
	//Liste de tout les points d'origine
	public ArrayList<Point> points;
	//tableau des Tree2D dont l'indice correspond a l'indice du point racine dans la liste points
	public Tree2D[] trees;

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

	//Floyd-Warshall
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
	//on a un tableau trees en variable global qui g??re ca
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
					//PROBLEM !!!
					//if(!root.getSubTrees().contains(dest)) root.getSubTrees().add(dest);
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

	//Kruskal 1
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

	//Kruskal 2
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

	//Union-Find
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


	//Kruskal 3
	public ArrayList<Line>  kruskal3(ArrayList<Line> aretes) {
		this.foret = new int[points.size()];
		for(int i=0; i<points.size(); i++) {
			this.foret[i]=-1;
		}
		ArrayList<Line> result = new ArrayList<Line>();
		aretes.sort( (Line a, Line b) -> {return (int) (distanceCarre(a.getP(), a.getQ()) - (int) distanceCarre(b.getP(), b.getQ()));} );
		for(Line l : aretes) {
			int x = find(this.foret, getIndice(points, l.getP()));
			int y = find(this.foret, getIndice(points, l.getQ()));
			if(x != y) {
				result.add(l);
				union(this.foret, getIndice(points, l.getP()), getIndice(points, l.getQ()));
			}
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

	public Tree2D calculSteinerTME(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {

		this.points=points;
		this.trees = new Tree2D[points.size()];
		int[][] paths = calculShortestPaths(points, edgeThreshold);

		ArrayList<Line> aretes = new ArrayList<Line>();
		for(int i=0; i<hitPoints.size()-1; i++) {
			Point P = hitPoints.get(i);
			for(int j=0; j<hitPoints.size()-1; j++) {
				Point Q = hitPoints.get(j);
				int p = getIndice(points, P);
				int q = getIndice(points, Q);
				if(p!=q) {
					while (p!=q) {
						aretes.add(new Line(points.get(p),points.get(paths[p][q])));
						p = paths[p][q];
					}
				}
			}
		}

		Line head = aretes.get(0);
		Tree2D tmp1 = new Tree2D(head.getQ(), new ArrayList<>());
		trees[getIndice(points, head.getQ())]= tmp1;

		ArrayList<Tree2D> tmp2 = new ArrayList<Tree2D>();
		tmp2.add(tmp1);
		this.Tree = new Tree2D(head.getP(), tmp2);
		trees[getIndice(points, head.getP())]= this.Tree;

		aretes = kruskal1(aretes);
		LineToTree(aretes);
		return this.Tree;
	}

	 */






	//----------------------------------------
	//----------------------------------------
	//------------VERSION DIJKSTRA------------
	//----------------------------------------
	//----------------------------------------

	//Ci-dessous la version final qui fonctionne correctement.
	//On utilise le type var disponible depuis Java 10
	//qui est automatique.

	public void addEdge(HashMap<Integer, ArrayList<Integer>> edges, int i, int j) {
		var list = edges.get(i);
		if(list == null) {
			list = new ArrayList<Integer>();
			edges.put(i, list);
		}
		list.add(j);
	}


	// On pr??calcule les ar??tes du graphe pour les mettre dans une hashmap pour un acc??s rapide
	// La hashmap prend en cl?? l'indice d'un point et donne en valeur une liste des indices des points connect??s
	public HashMap<Integer, ArrayList<Integer>> createGeometricGraph(ArrayList<Point> points, int edgeThreshold) {
		var edges = new HashMap<Integer,ArrayList<Integer>>();

		for(int i = 0; i < points.size(); i++) {
			for(int j = 0; j < points.size(); j++) {
				if (points.get(i).distance(points.get(j)) < edgeThreshold) {
					addEdge(edges, i, j); // On ajoute les ar??tes dans les deux sens
					addEdge(edges, j, i);
				}
			}
		}

		return edges;
	}

	// Sert ?? la fois ?? la priority queue et ?? la hashmap, malgr?? une utilit?? diff??rente du champ point
	class DijkstraData {
		public DijkstraData(int point, double distance) {
			this.point = point;
			this.distance = distance;
		}
		int point; // le point actuel pour la priority queue ou le point pr??c??dent dans la hashmap
		double distance; // distance au point de d??part de la recherche
	}

	// Impl??mentation de l'algorithme de Dijkstra qui s'arr??te au premier point de l'arbre trouv?? (celui le plus pr??t, car on le trouve dans la priority queue des points les plus proches)
	public double shortestPathToTree(ArrayList<Point> points, HashMap<Integer, ArrayList<Integer>> edges, HashSet<Integer> treePoints, int start, ArrayList<Integer> path) {

		// Priority queue qui stocke les points et leur distance du d??part, et qui donne en premier le point le plus proche du point de d??part
		var pq = new PriorityQueue<DijkstraData>((a, b) -> {return a.distance < b.distance ? -1 : 1;});
		// Hashmap qui stocke pour chaque point visit?? sa distance du d??part et le point pr??c??dent dans son chemin
		var map = new HashMap<Integer, DijkstraData>();

		pq.add(new DijkstraData(start, 0));
		map.put(start, new DijkstraData(-1, 0));

		while(!pq.isEmpty()) {
			// Le point actuel encore non visit?? le plus proche du d??part
			var u = pq.poll();

			// S'il est contenu dans la liste des points de l'arbre, on calcule le path et on retourne la distance
			// On sait donc que c'est le chemin le plus court jusqu'au graphe
			if(treePoints.contains(u.point)) {
				int p = u.point;
				while(p != -1) {
					path.add(p);
					p = map.get(p).point; // On stocke l'ar??te pr??c??dente dans la hashmap
				}
				return u.distance;
			}

			// Pour tous les voisins de u
			var adjacents = edges.get(u.point);
			for(int adjacent : adjacents) {
				// On calcule la distance de adjacent au d??part, en passant par u
				double distance = u.distance + points.get(u.point).distance(points.get(adjacent));

				var v = map.get(adjacent);
				if(v == null || v.distance > distance) { // si v n'a pas encore ??t?? explor?? ou si la nouvelle distance est plus courte
					v = new DijkstraData(u.point, distance);
					map.put(adjacent, v);
					pq.add(new DijkstraData(adjacent, v.distance));
				}

			}

		}

		return -1;

	}

	// Trouver un point sp??cifique dans l'arbre, pour r??cup??rer son Tree2D
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

	// ajouter dans l'arbre un path calcul?? dans le dijkstra
	public Tree2D addPathToTree(Tree2D tree, HashSet<Integer> treePoints, ArrayList<Point> points, ArrayList<Integer> path) {
		var pathTree = findPointInTree(tree, points.get(path.get(0)));

		for(int i = 1; i<path.size(); i++) {
			int p = path.get(i);
			treePoints.add(p);
			var ntree = new Tree2D(points.get(p), new ArrayList<>());
			pathTree.getSubTrees().add(ntree);
			pathTree = ntree;
		}

		return tree;

	}

	// trouver l'indice d'un point (on utilise que des int)
	public int findPoint(ArrayList<Point> points, Point point) {
		for(int i = 0; i<points.size(); i++) {
			var p = points.get(i);
			if(p.equals(point)) {
				return i;
			}
		}
		return -1;
	}


	public Tree2D calculSteinerDijktra(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints, int budget) {

		// Hashmap contenant les ar??tes
		var edges = createGeometricGraph(points, edgeThreshold);

		// HashSet contenant tous les points de l'arbre
		var treePoints = new HashSet<Integer>();

		var start = hitPoints.get(0);
		int startIndex = findPoint(points, start);

		Tree2D tree = new Tree2D(start, new ArrayList<>());
		treePoints.add(startIndex);

		double totalDistance = 0;

		// tant qu'il y a de points ?? ajouter (au maximum)
		for(int h = 1; h<hitPoints.size(); h++) {

			// On cherche le point le plus pr??s de l'arbre, soit la distance minimale retourn??e par shortestPathTotree, et son path
			double minDistance = 0;
			ArrayList<Integer> bestPath = new ArrayList<>();

			for(int i = 0; i<hitPoints.size(); i++) {
				var hitPoint = findPoint(points, hitPoints.get(i));
				if(treePoints.contains(hitPoint)) continue;

				var path = new ArrayList<Integer>();
				double distance = shortestPathToTree(points, edges, treePoints, hitPoint, path);
				//System.out.println("point " + i + " distance " + distance + " pathsize " + path.size());

				if((minDistance == 0.0 || distance < minDistance) && distance != -1) {
					minDistance = distance;
					bestPath = path;
				}
			}

			// Il n'y a plus rien ?? ajouter
			if(minDistance == 0.0) break;

			totalDistance += minDistance;

			// Si la distance minimale d??passe le budget alors il n'y a plus de point ?? ajouter
			if(totalDistance > budget && budget > 0) {
				break;
			}

			//System.out.println("adding point " + (h+1) + " distance " + minDistance + " pathsize " + bestPath.size());

			tree = addPathToTree(tree, treePoints, points, bestPath);

		}

		return tree;
	}

	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		return calculSteinerDijktra(points, edgeThreshold, hitPoints, -1);
	}

	public Tree2D calculSteinerBudget(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
		return calculSteinerDijktra(points, edgeThreshold, hitPoints, 1664);
	}
}
