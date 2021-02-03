package ru.otus.collections.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

//TODO please FIXME with BlockingQueue
// Вопросы:
// - Что делает это многопоточное приложение?
// - Какие есть проблемы в данном многопоточном приложении?
// - Запустим приложение прямо сейчас!
// - Фиксим тест сейчас!
// - *Для какого сценария по нагрузке больше всего подходит BlockingQueue?
public class FixMe5WithBlockingQueueTest {

    private static final int ITERATIONS_COUNT = 1000;

    @Test
    public void testBlockingQueueWorksGreat() throws InterruptedException {

        List<Integer> list = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    list.remove(0);
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    list.add(list.size(), 5);
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });

        t1.start();
        t2.start();

        latch.countDown();

        t1.join();
        t2.join();

        assertThat(throwables).withFailMessage(throwables.toString()).isEmpty();
    }
}
