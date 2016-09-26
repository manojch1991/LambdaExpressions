package com.hw;


public class RealPizza implements Pizza{

	private String realPizza;
	RealPizza(){
		System.out.println("A real pizza is made!");
		realPizza = "REAL PIZZA!";
	}
	
	@Override
	public String getPizza() {
		return realPizza;
	}
}
