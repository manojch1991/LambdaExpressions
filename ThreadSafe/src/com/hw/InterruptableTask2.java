package com.hw;

import java.util.concurrent.locks.ReentrantLock;

public class InterruptableTask2 implements Runnable{
	
	ReentrantLock lock = new ReentrantLock();
	public void run(){
		lock.lock();
		try{
			while( !Thread.interrupted() ){
				if(Thread.interrupted())
					break;
				System.out.println(1);
			}
			System.out.println(4);
		}finally{
			lock.unlock();
		}
	}
	
	public static void main(String[] args){
		Thread t = new Thread(new InterruptableTask2());
		t.start();
		try
		{
			Thread.sleep(2000);
			t.interrupt();
		} catch (InterruptedException e){}
	}
}
