package com.hw;

public class ThreadPool implements Runnable {

    private TinyHttpd3 tinyHttpd3;
    public ThreadPool(TinyHttpd3 tinyHttpd3) {
        this.tinyHttpd3 = tinyHttpd3;
    }

    
    public void run() {
        tinyHttpd3.execute();
    }
}
