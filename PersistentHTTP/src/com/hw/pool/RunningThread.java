package com.hw.pool;

import java.util.concurrent.locks.ReentrantLock;

import com.hw.server.TinyHttpd;

public class RunningThread implements Runnable {

    ReentrantLock lock = new ReentrantLock();
    private TinyHttpd tinyHttpServer;

    public RunningThread(TinyHttpd tinyHttpServer) {
        this.tinyHttpServer = tinyHttpServer;
    }

    public void run() {
        try {
            lock.lock();
            tinyHttpServer.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
