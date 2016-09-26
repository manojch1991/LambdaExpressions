package com.hw;

import java.util.Date;
 
public class HelloWorldTest{
	private static String greeting;
	public static void main(String[] args){
		greeting = "Hello World";
		Thread thread = new Thread(() -> {
			for( int i=0; i<10; i++ ){
				try{
					Date now = new Date();
					System.out.println(now + " " + greeting);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		
	}
}
