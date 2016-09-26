package com.hw;

public class TestClass {

	public static void main(String args[]){
		RequestHandler handler = new RequestHandler();
		
		for(int i = 0; i < 10; i++){
			Thread t = new Thread(handler);
				t.start();
		}
	}
}
