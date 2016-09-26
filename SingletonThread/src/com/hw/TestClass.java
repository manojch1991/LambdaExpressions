package com.hw;

public class TestClass {

	public static void main(String[] args) {

		Thread t1= new Thread(()-> {
			System.out.println(Singleton.getInstance());
		});
		
		Thread t2= new Thread(()-> {
			System.out.println(Singleton.getInstance());
		});
		Thread t3= new Thread(()-> {
			System.out.println(Singleton.getInstance());
		});
		
		t1.start();
		t2.start();
		t3.start();
	}
}
