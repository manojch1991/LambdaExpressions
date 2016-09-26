package com.hw;

public class AutoSaver implements Runnable{

	File file = null;
	public AutoSaver(File file) {
		this.file = file;
	}

	@Override
	public void run() {
		try {
			file.save();
			Thread.sleep(2000);
			System.out.println("File is auto saved");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
