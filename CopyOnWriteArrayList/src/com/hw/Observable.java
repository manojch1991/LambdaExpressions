package com.hw;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Observable {

	private boolean changed = false;
	private CopyOnWriteArrayList<Observer>observers;
	ReentrantLock lock = new ReentrantLock();
	
	public Observable(){
		observers = new CopyOnWriteArrayList<Observer>();
	}
	
	public void addObserver(Observer o){
			if (o == null)
	            throw new NullPointerException();
	        if (!observers.contains(o)) 
	        	observers.add(o);
	}
	
	public void deleteObserver(Observer o){
			observers.remove(o);
	}
	
	protected void setChanged(){
			changed = true;
	}
	
	protected void clearChanged(){
			changed = false;
	}
	
	public boolean hasChanged(){
			return changed;
	}
	
	public void notifyObserver(Object obj){
		lock.lock();
		if(!changed) return;
		changed = false;
		lock.unlock();
		Iterator it = observers.iterator();
		while( it.hasNext() ){
		((Observer) it.next()).update(this, obj);
		}
		
	}
}
