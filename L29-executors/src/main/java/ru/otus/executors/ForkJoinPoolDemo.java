package ru.otus.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolDemo {
    private static final Logger logger = LoggerFactory.getLogger(ForkJoinPoolDemo.class);

    public static void main(String[] args) {
        new ForkJoinPoolDemo().go();
    }

    private void go() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        int result = forkJoinPool.invoke(new Task(new int[]{1, 2, 1, 1, 2, 3, 5}));
        logger.info("result:{}", result);
    }

    public static class Task extends RecursiveTask<Integer> {
        private final int[] data;

        Task(int[] data) {
            this.data = data;
            logger.info("data:{}", data);
        }

        @Override
        protected Integer compute() {
            List<Task> subTasks = new ArrayList<>();
            if (this.data.length > 1) {
                Task taskL = new Task(Arrays.copyOfRange(this.data, 0, this.data.length / 2));
                taskL.fork();
                subTasks.add(taskL);

                Task taskR = new Task(Arrays.copyOfRange(this.data, this.data.length / 2, this.data.length));
                taskR.fork();
                subTasks.add(taskR);

                Integer result = 0;
                for (Task subTask : subTasks) {
                    result += subTask.join();
                }
                return result;
            } else {
                return this.data[0];
            }
        }
    }
}
