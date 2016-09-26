package com.hw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public abstract class FileCache {
	public final static int MAX_CACHE_SIZE = 10;
	static HashMap<Path, String> cache = new HashMap<Path, String>();
	public abstract void replace(Path file);
	ReentrantLock lock = new ReentrantLock();
	public String fetch(Path file){
		lock.lock();
		try{
			String content = null;
			if(cache.containsKey(file)){
				content = cache.get(file);
				return content;
			}
			else{
				return cacheFile(file);
			}
		}finally{
			lock.unlock();
		}
	}
	
	public String cacheFile(Path file){
		lock.lock();
		try{
			if(cache.size() > MAX_CACHE_SIZE){
				replace(file);
			}
			else{
				String content;
				try {
					content = new String(Files.readAllBytes(file));
					cache.put(file, content);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return cache.get(file);
		}finally{
			lock.unlock();
		}
	}
	
}
