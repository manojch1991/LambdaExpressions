package com.hw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheLFU extends FileCache{
	AccessCounter accessCounter = new AccessCounter();
	ReentrantLock lock = new ReentrantLock();
	@Override
	public void replace(Path file) {
		if(cache.size() > MAX_CACHE_SIZE){
			lock.lock();
			try{
				int min = 0;
				for(Path path : cache.keySet()){
					if(min > accessCounter.getCount((Path) path)){
						min = accessCounter.getCount((Path) path);
					}
					String content = null;
					try {
						Files.readAllBytes(file);
						content = new String(Files.readAllBytes(file));
						cache.put(file, content);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}finally{
				lock.unlock();
			}
		}
		else{
			lock.lock();
			try{
				String content = null;
				try {
					Files.readAllBytes(file);
					content = new String(Files.readAllBytes(file));
					cache.put(file, content);
				} catch (IOException e) {
					e.printStackTrace();
				}
				}finally{
					lock.unlock();
			}
		}
	}
}
