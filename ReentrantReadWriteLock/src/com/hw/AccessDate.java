package com.hw;

import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccessDate {

	HashMap<Path,Date> path = new HashMap<Path,Date>();
	int counter;
	ReentrantLock lock = new ReentrantLock();
	Date newDate;
	Date lastDate;
	
	public void putNewDate(Path p){
		lock.lock();
		try{
			newDate = new Date();
			if(path.containsKey(p)){
				path.replace(p, newDate);
			}
			else{
				path.put(p, newDate);
			}
			System.out.println(p.toUri());
		}finally{
			lock.unlock();
		}
	}
	
	public Date getDate(Path p){
		lock.lock();
		try{
			if(path.containsKey(p)){
				lastDate = path.get(p);
				return lastDate;
			}
			else 
				return null;
		}finally{
			lock.unlock();
		}
	}
}
