package com.hw;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccessCounter {

	HashMap<Path,Integer> path = new HashMap<Path,Integer>();
	int counter;
	ReentrantLock lock = new ReentrantLock();
	ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	Lock readLock = readWriteLock.readLock();
	Lock writeLock = readWriteLock.writeLock();
	
	public void increment(Path p){
		writeLock.lock();
		try{
			if(path.containsKey(p)){
				counter = path.get(p).intValue();
				counter += 1;
				path.replace(p, counter);
			}
			else{
				path.put(p, 1);
			}
			System.out.println(p.toUri());
		}finally{
			writeLock.unlock();
		}
	}
	
	public int getCount(Path p){
		readLock.lock();
		try{
			if(path.containsKey(p)){
				counter = path.get(p).intValue();
				return counter;
			}
			else 
				return 0;
		}finally{
			readLock.unlock();
		}
	}
}
