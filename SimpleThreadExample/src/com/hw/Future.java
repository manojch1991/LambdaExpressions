package com.hw;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Future implements Pizza{

	private RealPizza realPizza = null;
	private ReentrantLock lock;
	private Condition ready;
	
	public Future(){
		lock = new ReentrantLock();
		ready = lock.newCondition();
		System.out.println("An order is made.");
	}
	
	public void setRealPizza( RealPizza real ){
		lock.lock();
		try{
			if( realPizza != null ){ 
				return; 
			}
			realPizza = real;
			ready.signalAll();
		}finally{
			lock.unlock();
		}
	}
	
	public String getPizza(){
		String pizzaData = null;
		lock.lock();
		try{
			if( realPizza == null ){
				try {
					ready.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pizzaData = realPizza.getPizza();
		}finally{
			lock.unlock(); 
		}
		return pizzaData;
	}
	
	public String getPizza(long timeout ) throws TimeoutException{
		String pizzaData = null;
		lock.lock();
		try{
			if( realPizza == null ){
				try {
					ready.await(timeout, TimeUnit.MILLISECONDS);
					if(realPizza == null){
						throw new TimeoutException();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pizzaData = realPizza.getPizza();
		}finally{
			lock.unlock(); 
		}
		return pizzaData;
	}
	
	
	public boolean isReady(){
		if(realPizza != null)
			return true;
		else 
			return false;
	}

}
