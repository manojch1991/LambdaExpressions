package com.hw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileCacheLRURW extends FileCache{
	AccessDate accessDate = new AccessDate();
	ReentrantLock lock = new ReentrantLock();
	ReentrantReadWriteLock readLock = new ReentrantReadWriteLock();
	ReentrantReadWriteLock writeLock = new ReentrantReadWriteLock();
	@Override
	public void replace(Path file) {
		if(cache.size() > MAX_CACHE_SIZE){
			lock.lock();
			try{
				Date minDate = new Date();
				for(Path path : cache.keySet()){
					if((minDate.compareTo(accessDate.getDate(path))) > 0){
						minDate = accessDate.getDate(file);
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
			String content = null;
			lock.lock();
			try{
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
	
	public String fetch(Path file){
		String content = null;
		writeLock.writeLock().lock();
		try{
			if(!cache.containsKey(file)){
				return cacheFile(file);
			}
			readLock.readLock().lock();
			writeLock.writeLock().unlock();
			content = cache.get(file);
			return content;
		}finally{
			readLock.readLock().unlock();
		}
	}
	
	
	
	
	
	
	
}
