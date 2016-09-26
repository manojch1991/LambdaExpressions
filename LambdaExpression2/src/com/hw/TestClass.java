package com.hw;
import java.awt.Point;
import java.util.ArrayList;


public class TestClass {

	public static void main(String[] args) {

		ArrayList<Point> al = new ArrayList<Point>();
		al.add( new Point(5,1) ); 
		al.add( new Point(0,1) ); 
		al.add( new Point(0,5) );
//		Polygon p = new Polygon( al, new TriangleAreaCalc(al) );
		
		Polygon p = new Polygon(al, (ArrayList<Point> points) -> {
			{
				double area, s;
				points.get(0).getX();
				double ab = Math.sqrt(Math.pow((points.get(0).getX() - points.get(1).getX()), 2) + Math.pow((points.get(0).getY() - points.get(1).getY()), 2));
				double bc = Math.sqrt(Math.pow((points.get(1).getX() - points.get(2).getX()), 2) + Math.pow((points.get(1).getY() - points.get(2).getY()), 2));
				double ca = Math.sqrt(Math.pow((points.get(2).getX() - points.get(0).getX()), 2) + Math.pow((points.get(2).getY() - points.get(0).getY()), 2));
				s = (ab + bc + ca)/2;
				area = Math.sqrt(s * (s - ab) * (s - bc) * (s - ca));
				return (float) area;
				
			}
		});
		
		float triArea = p.getArea(); // triangle’s area
		System.out.println("Triangle are: " + triArea);
		p.addPoint( new Point(5,5) );
		p.setAreaCalc((ArrayList<Point> points)->{
			double area;
			double width, length;
			width = Math.sqrt((Math.pow(points.get(0).getX() + points.get(1).getX(), 2)) + Math.pow((points.get(0).getY() - points.get(1).getY()), 2));
			length = Math.sqrt((Math.pow(points.get(1).getX() + points.get(2).getX(), 2)) + Math.pow((points.get(1).getY() - points.get(2).getY()), 2));
			area = width * length;
			return (float) area;
		});
		
//		p.setAreaCalc( new RectangleAreaCalc(al) );
		float rectArea = p.getArea(); // rectangle’s area
		System.out.println("rectangle area: " + rectArea);
		
	}

}
