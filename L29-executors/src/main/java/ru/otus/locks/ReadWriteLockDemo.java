package ru.otus.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ReadWriteLockDemo {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteLockDemo.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private int sharedCounter = 0;

    public static void main(String[] args) {
        new ReadWriteLockDemo().go();
    }

    private void go() {
        new Thread(this::writer, "writer-1").start();
        new Thread(this::writer, "writer-2").start();

        for (int idx = 0; idx < 5; idx++) {
            new Thread(this::reader).start();
        }
    }

    private void writer() {
        while (!Thread.currentThread().isInterrupted()) {
            lock.writeLock().lock();
            try {
                this.sharedCounter++;
                sleep(3);
                this.sharedCounter++;
                logger.info("write:{}", sharedCounter);
            } finally {
                lock.writeLock().unlock();
            }
            sleep(2);
        }
    }

    private void reader() {
        while (!Thread.currentThread().isInterrupted()) {
           // logger.info("before lock");
            lock.readLock().lock();
            try {
                logger.info("read:{}", this.sharedCounter);
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
