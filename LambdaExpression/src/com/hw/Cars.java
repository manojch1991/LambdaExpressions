package com.hw;

import java.text.DecimalFormat;

public class Cars {
	private String carName;
	private double mileage;
	private double price;
	private int yearModel;
	private String color;
	private double dominate;

	DecimalFormat df = new DecimalFormat("####0.00");
	public Cars(String carName, double mileage, double price, int yearModel,
			String color) {
		super();
		this.carName = carName;
		this.mileage = mileage;
		this.price = price;
		this.yearModel = yearModel;
		this.color = color;
		this.dominate = Double.valueOf(df.format(price/mileage)); //convert to 2 decimal point
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public int getYearModel() {
		return yearModel;
	}

	public void setYearModel(int yearModel) {
		this.yearModel = yearModel;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getMileage() {
		return mileage;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDominateCount() {
		return dominate;
	}

	public void setDominate(double dominate) {
		this.dominate = dominate;
	}

}
