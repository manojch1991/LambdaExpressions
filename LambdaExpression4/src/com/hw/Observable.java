package com.hw;

import java.util.ArrayList;

public class Observable {

	private boolean changed = false;
	private ArrayList<Observer> observers;
	
	public Observable(){
		observers = new ArrayList<Observer>();
	}
	
	public void addObserver(Observer o){
		if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
        	observers.add(o);
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
		synchronized(this){
			if (!changed)
				return;
			arrLocal = observers.toArray();
			clearChanged();
		}
			for (int i = arrLocal.length-1; i>=0; i--)
				((Observer) arrLocal[i]).update(this, obj);
	}
	
}
