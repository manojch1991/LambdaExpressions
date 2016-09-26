package com.hw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

public class RequestHandler implements Runnable {

	AccessCounter accessCounter = new AccessCounter();
	AccessDate accessData = new AccessDate();
	FileCacheLRU fileCacheLRU = new FileCacheLRU();
	FileCacheLFU fileCacheLFU = new FileCacheLFU();
	FileCacheLFURW fileCacheLFURW = new FileCacheLFURW();
	FileCacheLRURW fileCacheLRURW = new FileCacheLRURW();
	Path[] path = { Paths.get("./test/abc.txt"),
					Paths.get("./test/home.html"), 
					Paths.get("./test/profile.txt")};
	ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void run() {
		lock.lock();
		try{
			for(int i = 0; i < path.length; i++) {
				accessCounter.increment(path[i]);
				accessData.putNewDate(path[i]);
				System.out.println("Access Counter");
				System.out.println(path[i] + " "+ accessCounter.getCount(path[i]));
				System.out.println("Access Date");
				System.out.println(path[i] + " " + accessData.getDate(path[i]));
			}
			for(int i=0; i<path.length; i++){
				fileCacheLFU.replace(path[i]);
				fileCacheLRU.replace(path[i]);
				String content = fileCacheLFU.fetch(path[i]);
				String contentLRU = fileCacheLRU.fetch(path[i]);
				String contentLFURW = fileCacheLFURW.fetch(path[i]);
				String contentLRURW = fileCacheLRURW.fetch(path[i]);
				System.out.println("\nAccess Counter content");
				System.out.println(content);
				System.out.println("\nAccess Date content");
				System.out.println(contentLRU);
				System.out.println("\nAccess Counter content with RW");
				System.out.println(contentLFURW);
				System.out.println("\nAccess Date content with RW");
				System.out.println(contentLRURW);
			}
			
		}finally{
			lock.unlock();
		}
	}
}
