package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	//   renvoie une pair de points de la liste, de distance maximum.
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

	// calculDiametreOptimise: ArrayList<Point> --> Line
	//   renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametreOptimise(ArrayList<Point> points) {
		if (points.size()<3) {
			return null;
		}

		Point p=points.get(1);
		Point q=points.get(2);

		ArrayList<Point> enveloppe = enveloppeConvexeJarvis(points);
		ArrayList<Line> paire = PairesAntipodales(enveloppe);
		Line max=paire.get(0);
		double dmax = distance(max.getP(), max.getQ());
		for(Line l : paire) {
			double i = distance(l.getP(), l.getQ());
			if(i>dmax) {
				max = l;
				dmax = i;
			}
		}
		return max;
	}

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

	// calculCercleMin: ArrayList<Point> --> Circle
	//   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
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

	public int prodvect (Point q, Point p, Point r) {
		return (p.x-q.x)*(r.y-q.y)-(p.y-q.y)*(r.x-q.x);
	}

	public boolean estSeparent(Point q, Point p, ArrayList<Point> points){
		Point first = points.get(0);
		float firstz = prodvect(q,p,first);
		for(Point r : points){
			if(r!=q && r!=p) {
				float z = prodvect(q,p,r);
				if(!((firstz>0 && z>0)||(firstz<0 && z<0))){
					return false;
				}
			}
		}
		return true;
	}

	public ArrayList<Point> enveloppeConvexeNaif(ArrayList<Point> points){

		HashMap<Integer, Point> ymin = new HashMap<Integer, Point>();
		HashMap<Integer, Point> ymax = new HashMap<Integer, Point>();
		//points = triPixel(ymin, ymax, points);

		ArrayList<Point> enveloppe = new ArrayList<Point>();

		for(Point p1 : points){
			for(Point p2 : points) {
				if(p1!=p2) {
					if(estSeparent(p1, p2, points)) {
						enveloppe.add(p1);
						enveloppe.add(p2);
					}
				}
			}

		}
		return enveloppe;
	}

	public ArrayList<Point> triPixel(HashMap<Integer, Point> ymin, HashMap<Integer, Point> ymax, ArrayList<Point> points ){	
		ArrayList<Point> tri = new ArrayList<Point>();

		for(Point p : points) {
			if(!ymin.containsKey(p.x) && !ymax.containsKey(p.x)) {
				ymin.put(p.x, p);

			}else if(ymin.containsKey(p.x) && !ymax.containsKey(p.x)) {
				if(p.y<ymin.get(p.x).y) {
					ymax.put(p.x, ymin.get(p.x));
					ymin.put(p.x, p);
				}else {
					ymax.put(p.x, p);
				}
			}else if(!ymin.containsKey(p.x) && ymax.containsKey(p.x)) {
				if(p.y>ymax.get(p.x).y) {
					ymin.put(p.x, ymax.get(p.x));
					ymax.put(p.x, p);
				}else {
					ymin.put(p.x, p);
				}
			}else {
				if(p.y>ymax.get(p.x).y) {
					ymax.put(p.x, p);
				}
				if(p.y<ymin.get(p.x).y) {
					ymin.put(p.x, p);
				}
			}
		}

		for(Entry<Integer, Point> entry : ymin.entrySet()) {
			Point value = entry.getValue();
			tri.add(value);
		}
		for(Entry<Integer, Point> entry : ymax.entrySet()) {
			Point value = entry.getValue();
			tri.add(value);
		}
		return tri;
	}

	double dot(Point p, Point q, Point r) {
		double distpq = p.distance(q);
		double distpr = p.distance(r);
		return ((p.x - q.x) * (p.x - r.x) + (p.y - q.y) * (p.y - r.y)) / distpq / distpr;
	}

	public ArrayList<Point> enveloppeConvexeJarvis(ArrayList<Point> points) {

		ArrayList<Point> enveloppe = new ArrayList<Point>();

		Point P = null;
		int minx = 1000000;

		for(var a : points) {
			if(a.x < minx) {
				P = a;
				minx = a.x;
			}
		}

		enveloppe.add(P);
		Point Q = null;

		for(var q : points) {
			Q = q;
			if(Q.equals(P)) continue;
			int z = 0;
			for(var r : points) {
				if(r.equals(P) || r.equals(Q)) continue;
				int v = prodvect(P, Q, r);
				if(v == 0) {
					if(P.distanceSq(Q) < P.distanceSq(r)) {
						Q = null;
						break;
					}
					continue;
				}
				if(z == 0) {
					z = v;
					continue;
				}
				if((long) z * v < 0) {
					Q = null;
					break;
				}
			}
			if(Q != null) break;
		}
		enveloppe.add(Q);

		while(true) {
			Point R = null;
			double max = 100000;
			
			for(var r : points) {
				var dot = dot(Q, P, r);
				if(dot < max) {
					max = dot;
					R = r;
				}
			}
			
			if(enveloppe.contains(R)) break;
			enveloppe.add(R);
			P = Q;
			Q = R;
		}
		return enveloppe;
	}


	public ArrayList<Point> enveloppeConvexeGraham(ArrayList<Point> points){

		HashMap<Integer, Point> ymin = new HashMap<Integer, Point>();
		HashMap<Integer, Point> ymax = new HashMap<Integer, Point>();
		triPixel(ymin, ymax, points);

		ArrayList<Point> enveloppe = new ArrayList<Point>();

		ArrayList<Point> min = new ArrayList<Point>();
		min.ensureCapacity(ymin.size());
		ArrayList<Point> max = new ArrayList<Point>();
		max.ensureCapacity(ymax.size());
		for(Entry<Integer, Point> entry : ymin.entrySet()) {
			Point value = entry.getValue();
			min.add(value);
		}
		for(Entry<Integer, Point> entry : ymax.entrySet()) {
			Point value = entry.getValue();
			max.add(value);
		}

		min.sort((Point a, Point b) -> {return b.x-a.x;});
		max.sort((Point a, Point b) -> {return a.x-b.x;});
		enveloppe.addAll(max);
		enveloppe.addAll(min);


		Point a = enveloppe.get(0);
		Point b = enveloppe.get(1);
		Point c = enveloppe.get(2);
		int cpt = 0;
		while(cpt<enveloppe.size()) {
			if(cpt==enveloppe.size()-2) {
				a = enveloppe.get(cpt);
				b = enveloppe.get(cpt+1);
				c = enveloppe.get(0);
			}else if(cpt==enveloppe.size()-1) {
				a = enveloppe.get(cpt);
				b = enveloppe.get(0);
				c = enveloppe.get(1);
			}else {
				a = enveloppe.get(cpt);
				b = enveloppe.get(cpt+1);
				c = enveloppe.get(cpt+2);
			}
			int z = prodvect(a,b,c);
			if(z>0) {
				enveloppe.remove(b);
				if(cpt>0) {
					cpt--;
				}
			}else {
				cpt++;
			}
		}
		return enveloppe;
	}
	
	public ArrayList<Point> filtrageAklToussaint(ArrayList<Point> tab, ArrayList<Point> points){
		
		ArrayList<Point> filtre = new ArrayList<Point>();
		for(int i=0;i<4;i++) {
			tab.add(points.get(0));
		}
		 for(Point p : points) {
			 if(p.y>tab.get(0).y) tab.set(0, p);
			 if(p.y<tab.get(1).y) tab.set(2, p);
			 if(p.x>tab.get(2).x) tab.set(1, p);
			 if(p.x<tab.get(3).x) tab.set(3, p);
		 }
		 for(Point p : points) {
			 int z1 = prodvect(tab.get(0),tab.get(1),p);
			 int z2 = prodvect(tab.get(1),tab.get(2),p);
			 int z3 = prodvect(tab.get(2),tab.get(3),p);
			 int z4 = prodvect(tab.get(3),tab.get(0),p);
			 if(!(z1<0 && z2<0 && z3<0 && z4<0)) filtre.add(p);
		 }		
		
		return filtre;
		
	}
	
	public void iter(ArrayList<Point> points) {
				
	}
	
	public ArrayList<Point> enveloppeConvexeQuickHull(ArrayList<Point> points){

		ArrayList<Point> enveloppe = new ArrayList<Point>();
		ArrayList<Point> tab = new ArrayList<Point>();
		points = filtrageAklToussaint(tab, points);
		
		for(int i=0; i<tab.size(); i++) {
			double taille = 0;
			for(Point p : points) {
				int z;
				if(i==tab.size()-1) {
					z = prodvect(tab.get(i),tab.get(0),p);
				}else {
					z = prodvect(tab.get(i),tab.get(i+1),p);
				}
				if(z>0  ) {
					
				}
			}
		}
		
		
		return enveloppe;
	}


	// enveloppeConvexe: ArrayList<Point> --> ArrayList<Point>
	//   renvoie l'enveloppe convexe de la liste.
	public ArrayList<Point> enveloppeConvexe(ArrayList<Point> points){
		if (points.size()<3) {
			return null;
		}
		//points = triPixel(new HashMap<Integer, Point>(), new HashMap<Integer, Point>(), points);
		return enveloppeConvexeJarvis(points);
	}
	
	
	public ArrayList<Line> PairesAntipodales (ArrayList<Point> e) {
		ArrayList<Line> list = new ArrayList<Line>();
		int n = e.size()-2;
		int k = 1;
		while((distPointDroite(e.get(k), e.get(n), e.get(0))) < (distPointDroite(e.get(k+1), e.get(n), e.get(0)))) {
			k++;
		}
		int i = 0;
		int j=k;
		while(i<k && j<n) {
			while(((distPointDroite(e.get(j), e.get(i), e.get(i+1))) < (distPointDroite(e.get(j+1), e.get(i), e.get(i+1)))) && j<n) {
				list.add(new Line(e.get(1), e.get(j)));
				j++;
			}
			list.add(new Line(e.get(1), e.get(j)));
			i++;
		}
		return list;
	}
	
	public double distPointDroite(Point r, Point p, Point q) {
		return prodvect(p, q, r)/distance(p, q);
	}
	

}
