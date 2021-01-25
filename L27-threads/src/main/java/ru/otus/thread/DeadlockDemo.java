package ru.otus.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DeadlockDemo {
    private static final Logger logger = LoggerFactory.getLogger(DeadlockDemo.class);

    private final Resource r1 = new Resource("R1");
    private final Resource r2 = new Resource("R2");

    public static void main(String[] args) {
        new DeadlockDemo().demo();
    }

    private void demo() {
        Thread t1 = new Thread(() -> action(r1, r2));
        t1.setName("t1");

        Thread t2 = new Thread(() -> action(r2, r1));
        t2.setName("t2");

        t1.start();
        t2.start();


        sleep();
        sleep();
        logger.info("findDeadlockedThreads");
        long[] threads = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
        if (threads != null) {
            ThreadInfo[] threadInfo = ManagementFactory.getThreadMXBean().getThreadInfo(threads);
            System.out.println(Arrays.toString(threadInfo));
        }
    }


    private static void action(Resource has, Resource need) {
        logger.info("{} has: {}", Thread.currentThread().getName(), has);
        synchronized (has) {
            sleep();
            logger.info("{} taking: {}", Thread.currentThread().getName(), need);
            synchronized (need) {
                logger.info("taken by {}", Thread.currentThread().getName());
            }
        }
    }


    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    class Resource {
        private final String name;

        Resource(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Resource{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
