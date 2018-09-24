/***************************************
 * Michael W. 2018 August 13
 * This file implements brute-force
 * range search and nearest-neighbor
 * search for a unit square.
 **************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET
{
	private TreeSet<Point2D> points; 
	
	public PointSET()
	{
		points = new TreeSet<Point2D>();
	}
	
	public boolean isEmpty()                      // is the set empty? 
	{
		return points.isEmpty();
	}
	
	public int size()                         // number of points in the set 
	{
		return points.size();
	}
	
	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null) throw new java.lang.IllegalArgumentException();
		if (!points.contains(p)) points.add(p);
	}
	
	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null) throw new java.lang.IllegalArgumentException();
		return points.contains(p);
	}
	
	public void draw()                         // draw all points to standard draw 
	{
		StdDraw.setPenColor(StdDraw.BLACK); 
		StdDraw.setPenRadius(0.01);
		for (Point2D p : points)
			p.draw();
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
	{
		if (rect == null) throw new java.lang.IllegalArgumentException();
		ArrayList<Point2D> inside = new ArrayList<Point2D>();
		for (Point2D p : points)
		{
			if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax())
				inside.add(p);
		}
		
		return inside;
	}
	
	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	{
		if (p == null) throw new java.lang.IllegalArgumentException();
		if (this.isEmpty()) return null;
		
		Point2D closest = points.first();
		double distance = p.distanceSquaredTo(closest);
		for (Point2D a : points)
			if (a.distanceSquaredTo(p) < distance)
			{	
				closest = a;
				distance = p.distanceSquaredTo(closest);
			}
		return closest;
	}
	
	public static void main(String[] args)                  // unit testing of the methods (optional)
	{
		
	}
}