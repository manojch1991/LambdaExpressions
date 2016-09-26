package com.hw;

import java.awt.Point;
import java.util.ArrayList;

public class TestClass {

	public static void main(String[] args) {

		ArrayList<Point> al = new ArrayList<Point>();
		al.add( new Point(5,1) ); 
		al.add( new Point(0,1) ); 
		al.add( new Point(0,5) );
		
		Polygon p = new Polygon(al);
		p.getArea();
		System.out.println("Triangle area: " + p.getArea());
		al.add(new Point(5,5));
		System.out.println("Rectangle area: " + p.getArea());
	}
}
