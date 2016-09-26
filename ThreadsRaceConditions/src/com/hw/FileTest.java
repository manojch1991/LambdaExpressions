package com.hw;

public class FileTest {

	public static void main(String[] args) {

		File file = new File();
		Editor editor = new Editor(file);
		AutoSaver autoSaver = new AutoSaver(file);
		Thread thread1 = new Thread(editor);
		Thread thread2 = new Thread(autoSaver);
		thread1.start();
		thread2.start();
	}

}
