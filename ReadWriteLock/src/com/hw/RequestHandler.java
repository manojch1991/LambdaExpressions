package com.hw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

public class RequestHandler implements Runnable {

	AccessCounter accessCounter = new AccessCounter();
	Path[] path = { Paths.get("index.html"), 
					Paths.get("image.png"), 
					Paths.get("home.html")};
	ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void run() {
		lock.lock();
		try{
			for(int i = 0; i < path.length; i++) {
				accessCounter.increment(path[i]);
				System.out.println(path[i] + " "+ accessCounter.getCount(path[i]));
			}
		}finally{
			lock.unlock();
		}
	}
}
