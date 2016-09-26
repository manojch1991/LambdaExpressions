package com.hw;

public class Guest implements Runnable{
	
	private SecurityGate gate;
	
	public Guest() {
		gate = SecurityGate.getInstance();
	}

	@Override
	public void run() {
		gate.enter();
		gate.exit();
		gate.getCount();
		System.out.println(gate.getCount());
	}

}
