/***************************************
 * Michael W. 2018 August 13
 * This file implements 2d-tree-based
 * range search and nearest-neighbor
 * search for a unit square.
 **************************************/


import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;

public class KdTree
{
	private static final boolean X_VAL = true;
	
	private Node root;
	private int size; // number of points
	
	private static class Node
	{
		private Point2D p;
		private RectHV rect;
		private Node lb; // left branch
		private Node rb; // right branch
	}
	
	public KdTree()
	{
		size = 0;
		root = null;
	}
	
	public boolean isEmpty()                      // is the set empty? 
	{
		return size == 0;
	}
	
	public int size()                         // number of points in the set 
	{
		return size;
	}
	
	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null) throw new java.lang.IllegalArgumentException();
		if (this.contains(p)) return;
		if (root == null)
		{
			root = new Node();
			root.p = p;
			root.rect = new RectHV(0, 0, 1, 1); // entire unit square
		} else
			insert(root, p, X_VAL);
		size++;
	}
	
	private void insert(Node n, Point2D pt, boolean x_key) // x_key determines whether x or y value is used as key
	{
		if (x_key)
		{
			if (pt.x() < n.p.x() && n.lb == null)
			{
				n.lb = new Node();
				n.lb.p = pt;
				n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
			} else if (pt.x() < n.p.x())
			{
				insert(n.lb, pt, !x_key);
			} else if (n.rb == null)
			{
				n.rb = new Node();
				n.rb.p = pt;
				n.rb.rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
			} else
			{
				insert(n.rb, pt, !x_key);
			}
		} else
		{
			if (pt.y() < n.p.y() && n.lb == null)
			{
				n.lb = new Node();
				n.lb.p = pt;
				n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
			} else if (pt.y() < n.p.y())
			{
				insert(n.lb, pt, !x_key);
			} else if (n.rb == null)
			{
				n.rb = new Node();
				n.rb.p = pt;
				n.rb.rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
			} else
			{
				insert(n.rb, pt, !x_key);
			}
		}
	}
	
	public boolean contains(Point2D p)            // does the set contain point p? 
	{
		if (p == null) throw new java.lang.IllegalArgumentException();
		Node cmp = root;
		boolean keyval = X_VAL;
		
		while (cmp != null)
		{
			if (cmp.p.equals(p))
			{
				return true;
			} else if (keyval)
			{
				if(p.x() < cmp.p.x())
					cmp = cmp.lb;
				else
					cmp = cmp.rb;
			} else
			{
				if(p.y() < cmp.p.y())
					cmp = cmp.lb;
				else
					cmp = cmp.rb;
			}
			keyval = !keyval;
		}
		return false;
	}
	
	public void draw()                         // draw all points to standard draw 
	{
		draw(root, X_VAL);
	}
	
	private void draw(Node n, boolean keyval)
	{
		if (n == null) return;
		
		if (keyval)
		{
			System.out.println("Red ymin: " + n.rect.ymin() + " ymax: " + n.rect.ymax());
			StdDraw.setPenRadius();
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
		} else
		{
			System.out.println("Blue xmin: " + n.rect.xmin() + " xmax: " + n.rect.xmax());
			StdDraw.setPenRadius();
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
		}
		
		StdDraw.setPenColor(StdDraw.BLACK); 
		StdDraw.setPenRadius(0.01);
		n.p.draw();
		draw(n.rb, !keyval);
		draw(n.lb, !keyval);
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary) 
	{
		ArrayList<Point2D> inside = new ArrayList<Point2D>();
		addInsideRect(inside, rect, root, X_VAL);
		return inside;
	}
	
	private void addInsideRect(ArrayList<Point2D> arry, RectHV rect, Node n, boolean keyval)
	{
		if (n == null) return;
		if (keyval)
		{
			if (Double.compare(rect.xmax(), n.p.x()) < 0)
			{
				addInsideRect(arry, rect, n.lb, !keyval);
			} else if (Double.compare(rect.xmin(), n.p.x()) > 0)
			{
				addInsideRect(arry, rect, n.rb, !keyval);
			} else if (Math.abs(rect.xmax() - n.p.x()) <= 0.0001)
			{
//				System.out.println("ORc");
				if (Double.compare(rect.ymax(), n.p.y()) >= 0 && Double.compare(rect.ymin(), n.p.y()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.lb, !keyval);
			} else if (Math.abs(rect.xmin() - n.p.x()) <= 0.0001)
			{
//				System.out.println("ORc");
				if (Double.compare(rect.ymax(), n.p.y()) >= 0 && Double.compare(rect.ymin(), n.p.y()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.rb, !keyval);
			} else
			{
				if (Double.compare(rect.ymax(), n.p.y()) >= 0 && Double.compare(rect.ymin(), n.p.y()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.lb, !keyval);
				addInsideRect(arry, rect, n.rb, !keyval);
			}
		} else 
		{
			if (Double.compare(rect.ymax(), n.p.y()) < 0)
			{
				addInsideRect(arry, rect, n.lb, !keyval);
			} else if (Double.compare(rect.ymin(), n.p.y()) > 0)
			{
				addInsideRect(arry, rect, n.rb, !keyval);
			} else if (Math.abs(rect.ymax() - n.p.y()) <= 0.0001)
			{
//				System.out.println("ORc");
				if (Double.compare(rect.xmax(), n.p.x()) >= 0 && Double.compare(rect.xmin(), n.p.x()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.lb, !keyval);
			} else if (Math.abs(rect.ymin() - n.p.y()) <= 0.0001)
			{
//				System.out.println("ORc");
				if (Double.compare(rect.xmax(), n.p.x()) >= 0 && Double.compare(rect.xmin(), n.p.x()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.rb, !keyval);
			} else
			{
				if (Double.compare(rect.xmax(), n.p.x()) >= 0 && Double.compare(rect.xmin(), n.p.x()) <= 0) arry.add(n.p);
				addInsideRect(arry, rect, n.lb, !keyval);
				addInsideRect(arry, rect, n.rb, !keyval);
			}
		}
	}
	
	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	{
		return closer(root, root, p, X_VAL).p;
	}
	
	private Node closer(Node current, Node toSearch, Point2D target, boolean keyval)
	{
		if (toSearch == null || (current.p.distanceSquaredTo(target) == 0)) return current;
		else if (toSearch.p.distanceSquaredTo(target) == 0) return toSearch;
		
		Node nearer, further;
		
		if (toSearch.p.distanceSquaredTo(target) < current.p.distanceSquaredTo(target)) current = toSearch;
		
		if (keyval)
		{
			nearer = (target.x() < toSearch.p.x())? toSearch.lb : toSearch.rb;
			further = (target.x() < toSearch.p.x())? toSearch.rb : toSearch.lb;
		} else
		{
			nearer = (target.y() < toSearch.p.y())? toSearch.lb : toSearch.rb;
			further = (target.y() < toSearch.p.y())? toSearch.rb : toSearch.lb;
		}
			
		if ((nearer != null) && nearer.rect.distanceSquaredTo(target) < current.p.distanceSquaredTo(target)) // if the closer side's rectangle is at least as close as the current closest point
		{
			current = closer(current, nearer, target, !keyval);
		}
		if ((further != null) && further.rect.distanceSquaredTo(target) < current.p.distanceSquaredTo(target))
		{
			current = closer(current, further, target, !keyval);
		} 
		
		return current;
	}
	
	public static void main(String[] args)                  // unit testing of the methods (optional)
	{
		
	}
}