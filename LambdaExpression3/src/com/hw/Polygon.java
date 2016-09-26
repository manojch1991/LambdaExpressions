package com.hw;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;


public class Polygon {

	private ArrayList<Point> points;
	AreaCalculator areaCalc;
	
	public Polygon(ArrayList<Point> points, AreaCalculator areaCalc) {
		this.points = points;
		this.areaCalc = areaCalc;
		this.points = Objects.requireNonNull(points);
		this.areaCalc = Objects.requireNonNull(areaCalc);
	}
	
	public Polygon(ArrayList<Point> points){
		this(points, (Polygon p) -> {
			if(points.size() == 3){
				double area, s;
				points.get(0).getX();
				double ab = Math.sqrt(Math.pow((points.get(0).getX() - points.get(1).getX()), 2) + Math.pow((points.get(0).getY() - points.get(1).getY()), 2));
				double bc = Math.sqrt(Math.pow((points.get(1).getX() - points.get(2).getX()), 2) + Math.pow((points.get(1).getY() - points.get(2).getY()), 2));
				double ca = Math.sqrt(Math.pow((points.get(2).getX() - points.get(0).getX()), 2) + Math.pow((points.get(2).getY() - points.get(0).getY()), 2));
				s = (ab + bc + ca)/2;
				area = Math.sqrt(s * (s - ab) * (s - bc) * (s - ca));
				return (float) area;
			}
			else if(points.size() == 4){
				double area;
				double width, length;
				width = Math.sqrt((Math.pow(points.get(0).getX() + points.get(1).getX(), 2)) + Math.pow((points.get(0).getY() - points.get(1).getY()), 2));
				length = Math.sqrt((Math.pow(points.get(1).getX() + points.get(2).getX(), 2)) + Math.pow((points.get(1).getY() - points.get(2).getY()), 2));
				area = width * length;
				return (float) area;
			}else{
				return 0;
			}
		});
	}

	public float getArea() {
		return areaCalc.getArea(this);
		
	}
}
