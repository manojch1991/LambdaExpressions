package com.hw;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SummationRunnable implements Runnable{
	private int upperBound;
	private boolean done = false;
	private static Lock lock = new ReentrantLock();

	public SummationRunnable(int n){
		upperBound = n;
	}
	public void setDone(){
		lock.lock();
		try{
			done = true;
		}finally{
			lock.unlock();
		}
	}
	public void run(){
		try{
			while( !done ){
				lock.lock();
				try{
					if(done) 
						break;
					System.out.println(upperBound);
					upperBound--;
					Thread.sleep(1000);
					if( upperBound<0 ){
						System.out.println("print done");
						return;
					}
				}finally{
					lock.unlock();
				}
			}
			System.out.println("stopped by main()!");
			done = false;
		} catch (InterruptedException e){}
	}
	
	public static void main(String[] args){
		SummationRunnable sRunnable = new SummationRunnable(10);
		Thread thread = new Thread(sRunnable);
		thread.start();
		for(int i=0; i<5; i++){
			System.out.println("#");
		}
		sRunnable.setDone();
		try{
			thread.join();
			new Thread(sRunnable).start();
		} catch (InterruptedException e){
		}
	}
}
