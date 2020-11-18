package ru.otus.composite;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class Marine implements Unit {
    @Override
    public void move() {
        System.out.println("Marine is moving");
    }
}
