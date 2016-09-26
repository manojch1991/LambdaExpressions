package com.hw;

public class Editor implements Runnable{

	File file = null;
	public Editor(File file) {
		this.file = file;
	}

	@Override
	public void run() {
		try {
			file.change();
			System.out.println("File is changed");
			file.save();
			System.out.println("File is saved");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}

}
