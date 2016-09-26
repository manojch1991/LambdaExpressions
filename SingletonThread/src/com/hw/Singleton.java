package com.hw;

import java.util.concurrent.locks.ReentrantLock;

public class Singleton {

		private Singleton(){
		}
		private static Singleton instance = null;
		private static ReentrantLock lock = new ReentrantLock();
		
		public static Singleton getInstance(){
		lock.lock();
		try{
			if(instance==null)
				instance = new Singleton ();
			return instance;
		} finally{
			lock.unlock();
		}
	}
}
