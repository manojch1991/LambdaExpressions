package com.hw;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Observable {

	private boolean changed = false;
	private ArrayList<Observer> observers;
	ReentrantLock lock = new ReentrantLock();
	
	public Observable(){
		observers = new ArrayList<Observer>();
	}
	
	public void addObserver(Observer o){
		lock.lock();
		try{
			if (o == null)
	            throw new NullPointerException();
	        if (!observers.contains(o)) 
	        	observers.add(o);
		}finally{
			lock.unlock();
		}
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
		Object[] arrLocal;
		lock.lock();
		try{
			if (!changed)
	            return;
	        arrLocal = observers.toArray();
	        clearChanged();
		}finally{
			lock.unlock();
		}
        
        for (int i = arrLocal.length-1; i>=0; i--)
        	((Observer) arrLocal[i]).update(this, obj);
	}
}
