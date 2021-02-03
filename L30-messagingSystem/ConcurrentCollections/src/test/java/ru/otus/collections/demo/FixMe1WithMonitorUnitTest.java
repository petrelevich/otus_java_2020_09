package ru.otus.collections.demo;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.System.out;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

//TODO please FIXME with monitor
// Вопросы:
// - Что делает это многопоточное приложение?
// - Какие есть проблемы в данном многопоточном приложении?
// - Запустим приложение прямо сейчас!
// - Из какого потока летит исключение?
// - Из какого метода летит исключение?
// - Какие есть варианты решения этой проблемы?
// - Какой объект может быть монитором?
// - Сделаем фикс прямо сейчас!
// - Какие есть проблемы в решении с монитором?
// - Какие проблемы остаются в коде?
// - *Для чего тут нужен CountDownLatch?
// - *Зачем вызывать join() на потоках?
public class FixMe1WithMonitorUnitTest {

    private static final int ITERATIONS_COUNT = 1000;

    @Test
    public void testMonitorWorksGreat() throws InterruptedException {

        final List<String> list = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> throwables = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    out.println("starting adding email " + i);
                    list.add(randomAlphabetic(10) + "@gmail.com");
                    out.println("finishing adding email " + i);
                }
            } catch (Throwable throwable) {
                throwables.add(throwable);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                for (int i = 0; i < ITERATIONS_COUNT; i++) {
                    out.println("starting read iteration " + i);
                    list.forEach(out::println);
                    out.println("finishing read iteration " + i);
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
