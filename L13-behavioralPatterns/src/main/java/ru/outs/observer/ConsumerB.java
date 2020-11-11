package ru.outs.observer;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class ConsumerB {

    /*
     * Большой и жирный класс со множеством полей
     *
     */


    private final Listener listener = data -> System.out.println("ConsumerB data:" + data);


    public Listener getListener() {
        return listener;
    }
}
