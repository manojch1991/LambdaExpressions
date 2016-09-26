package com.hw;

public class Test {

	public static void main(String[] args) {
		
		Guest guest = new Guest();
		Thread t1 = new Thread(guest);
		Thread t2 = new Thread(guest);
		Thread t3 = new Thread(guest);
		Thread t4 = new Thread(guest);
		Thread t5 = new Thread(guest);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
	}

}
