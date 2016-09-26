package com.hw.filecache;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileCacheLFU extends FileCache{
	AccessCounter accessCounter = new AccessCounter();
	@Override
	public void replace(Path file) {
		if(cache.size() > MAX_CACHE_SIZE){
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
		}
		else{
			String content = null;
			try {
				Files.readAllBytes(file);
				content = new String(Files.readAllBytes(file));
				cache.put(file, content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
