package pool;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.*;

public final class StaticThreadPool {

    private boolean debug = false;
    private WaitingRunnableQueue queue = null;
    private Vector<ThreadPoolThread> availableThreads = null;
    private static StaticThreadPool staticThreadPool = null;
    private static ReentrantLock lock = new ReentrantLock();
    private boolean stopped = false;

    private StaticThreadPool(int maxThreadNum, boolean debug) {
        this.debug = debug;
        queue = new WaitingRunnableQueue(this);
        availableThreads = new Vector<ThreadPoolThread>();
        for (int i = 0; i < maxThreadNum; i++) {
            ThreadPoolThread th = new ThreadPoolThread(this, queue, i);
            availableThreads.add(th);
            th.start();
        }
    }

    //
    public static StaticThreadPool getInstance(int maxThreadNum, boolean debug) {
        lock.lock();
        try {
            if (staticThreadPool == null) {
                staticThreadPool = new StaticThreadPool(maxThreadNum, debug);
                return staticThreadPool;
            } else {
                return staticThreadPool;
            }
        } finally {
            lock.unlock();
        }
    }
//

    public void shutdown() {
        lock.lock();
        try {
            stopped = true;
        } finally {
            lock.unlock();
        }
    }

    public void execute(Runnable runnable) {
        queue.put(runnable);
    }

    public int getWaitingRunnableQueueSize() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public int getThreadPoolSize() {
        return availableThreads.size();
    }

    private class WaitingRunnableQueue {

        private ArrayList<Runnable> runnables = new ArrayList<Runnable>();
        private StaticThreadPool pool;
        private ReentrantLock queueLock;
        private Condition runnablesAvailable;

        public WaitingRunnableQueue(StaticThreadPool pool) {
            this.pool = pool;
            queueLock = new ReentrantLock();
            runnablesAvailable = queueLock.newCondition();
        }

        public int size() {
            queueLock.lock();
            try {
                return runnables.size();
            } finally {
                queueLock.unlock();
            }
        }

        public void put(Runnable obj) {
            queueLock.lock();
            try {
                runnables.add(obj);
                if (pool.debug == true) {
                    System.out.println("A runnable queued.");
                }
                runnablesAvailable.signalAll();
            } finally {
                queueLock.unlock();
            }
        }

        public Runnable get() {
            queueLock.lock();
            try {
                while (runnables.isEmpty()) {
                    if (pool.debug == true) {
                        System.out.println("Waiting for a runnable...");
                    }
                    runnablesAvailable.await();
                }
                if (pool.debug == true) {
                    System.out.println("A runnable dequeued.");
                }
                return runnables.remove(0);
            } catch (InterruptedException ex) {
                return null;
            } finally {
                queueLock.unlock();
            }
        }
    }

    private class ThreadPoolThread extends Thread {

        private StaticThreadPool pool;
        private WaitingRunnableQueue queue;
        private int id;
        private ReentrantLock lock = new ReentrantLock();

        public ThreadPoolThread(StaticThreadPool pool, WaitingRunnableQueue queue, int id) {
            this.pool = pool;
            this.queue = queue;
            this.id = id;
        }

        public void run() {
            if (pool.debug == true) {
                System.out.println("Thread " + id + " starts.");
            }
            while (true) {
                if (stopped == true) {
                    return;
                }
                Runnable runnable = queue.get();
                if (runnable == null) {
                    if (pool.debug == true) {
                        System.out.println("Thread " + this.id + " is being stopped due to an InterruptedException.");
                    }
                    continue;
                } else {
                    if (pool.debug == true) {
                        System.out.println("Thread " + id + " executes a runnable.");
                    }
                    runnable.run();
                    if (pool.debug == true) {
                        System.out.println("ThreadPoolThread " + id + " finishes executing a runnable.");
                    }
                }
            }
        }
    }
}
