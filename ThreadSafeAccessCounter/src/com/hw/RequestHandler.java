package com.hw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RequestHandler implements Runnable {

	AccessCounter accessCounter = new AccessCounter();
	Path[] path = { Paths.get("./test/abc.txt"),
					Paths.get("./test/profile.txt"), 
					Paths.get("./test/home.html")
					};
	ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void run() {
		lock.lock();
		try{
			for(int i = 0; i < path.length; i++) {
				accessCounter.increment(path[i]);
				System.out.println(path[i] + " "+ accessCounter.getCount(path[i]));
				System.out.println("File path " + path[i].toUri());
				
				List<String> lines = Files.readAllLines(path[i]);
				for (String string : lines) {
					System.out.println("...." + string);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
}
