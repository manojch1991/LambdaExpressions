package com.hw;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SecurityGate {

	AtomicInteger counter = new AtomicInteger(0);
	static SecurityGate gate = null;
	static ReentrantLock lock = new ReentrantLock();
	
	private SecurityGate() {
		
	}
	public void enter(){
		counter.incrementAndGet();
	}
	public void exit(){
		counter.decrementAndGet();
	}
	public AtomicInteger getCount(){
		return counter;
	}
	public static SecurityGate getInstance() {
			if(gate == null){
				gate = new SecurityGate();
				return gate;
			}
			else 
				return gate;
	}
}
