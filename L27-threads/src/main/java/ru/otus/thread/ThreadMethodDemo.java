package ru.otus.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadMethodDemo {
    private static final Logger logger = LoggerFactory.getLogger(ThreadMethodDemo.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("starting");

        Thread thread = new Thread(
                () -> {
                    boolean stop = false;
                    while (!stop) {
                        logger.info("I am: {} state: {}", Thread.currentThread().getName(), Thread.currentThread().getState());
                        stop = sleepAndStop();
                        Thread.onSpinWait(); // "новая фича"
                    }
                });
        thread.setName("Named-thread");
        thread.setDaemon(false);
        logger.info("state: {}", thread.getState());

        thread.start();

        sleep();
        logger.info("interrupting");
        thread.interrupt();

        thread.join();

        logger.info("finished");
    }

    private static boolean sleepAndStop() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            return false;
        } catch (InterruptedException e) {
            logger.info("somebody is trying to stop us, Ok");
            Thread.currentThread().interrupt();
            return true;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
