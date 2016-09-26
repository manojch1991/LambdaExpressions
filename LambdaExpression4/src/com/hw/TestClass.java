package com.hw;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

public class TestClass {

	public static void main(String[] args) {

		String ticker = "Google";
		float quote = 100;
		Observable observerable = new Observable();
		StockQuoteObservable stockQuoteObservable = new StockQuoteObservable();
	
		Observer pieChartObserver = (observerable1, arg)-> {
			((StockEvent) arg).getTicker();
			((StockEvent) arg).getQuote();
			System.out.println("\nPie chart Representation: " + "Stock: " + ((StockEvent) arg).getTicker() + " Quote: " + ((StockEvent) arg).getQuote());

		};
		
		Observer tableObserver = (observerable1, arg)-> {
			((StockEvent) arg).getTicker();
			((StockEvent) arg).getQuote();
			System.out.println("\nTable chart Representation: " + "Stock: " + ((StockEvent) arg).getTicker() + " Quote: " + ((StockEvent) arg).getQuote());

		};
		
		Observer threeDObserver = (observerable1, arg)-> {
			((StockEvent) arg).getTicker();
			((StockEvent) arg).getQuote();
			System.out.println("\n3D chart Representation: " + "Stock: " + ((StockEvent) arg).getTicker() + " Quote: " + ((StockEvent) arg).getQuote());

		};

		
		Map<String, Float> map = new HashMap<>();
		
		stockQuoteObservable.addObserver(pieChartObserver);
		stockQuoteObservable.addObserver(tableObserver);
		stockQuoteObservable.addObserver(threeDObserver);
		
		map.put("Google", (float) 100.87);
		map.put("Yahoo", (float) 50.56);
		map.put("Amazon", (float)250.74);
		map.put("Apple", (float)150.32);
		
		Iterator<Map.Entry<String, Float>> entries = map.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry<String, Float> entry = entries.next();
				ticker = entry.getKey();
				quote = entry.getValue();
				stockQuoteObservable.changeQuote(ticker,quote);
		}
	}
}
