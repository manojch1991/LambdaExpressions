package com.hw;

import java.util.HashMap;
import java.util.Map;

public class StockQuoteObservable extends Observable{

	Map<String, Float> map = new HashMap<String, Float>();

	
	void changeQuote(String t, float q){
		map.put(t, q);
		setChanged();
		notifyObserver(new StockEvent(t, q));
	}
}
