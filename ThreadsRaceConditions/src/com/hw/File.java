package com.hw;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class File{

	private boolean changed = false;
	ReentrantLock lock = new ReentrantLock();
	
	public void change(){
		lock.lock();
		try{
			System.out.println("File content changed");
			changed = true;
		}finally{
			lock.unlock();
		}
	}
	
	public void save(){
		lock.lock();
		try{
			if (changed==false) 
				return;
			if (changed==true){
				System.out.println(new Date());
			}
			changed = false;
		}finally{
			lock.unlock();
		}
		
	}
	
}
