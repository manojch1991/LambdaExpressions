package com.hw;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccessCounter {

	HashMap<Path,Integer> path = new HashMap<Path,Integer>();
	int counter;
	ReentrantLock lock = new ReentrantLock();
	
	public void increment(Path p){
		lock.lock();
		try{
			if(path.containsKey(p)){
				counter = path.get(p).intValue();
				counter += 1;
				path.replace(p, counter);
			}
			else{
				path.put(p, 1);
			}
		}finally{
			lock.unlock();
		}
	}
	
	public int getCount(Path p){
		lock.lock();
		try{
			if(path.containsKey(p)){
				counter = path.get(p).intValue();
				return counter;
			}
			else 
				return 0;
		}finally{
			lock.unlock();
		}
	}
}
