package ru.otus.facade;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class Facade {
    private final HellSystemA hellSystemA;
    private final HellSystemB hellSystemB;

    public Facade(HellSystemA hellSystemA, HellSystemB hellSystemB) {
        this.hellSystemA = hellSystemA;
        this.hellSystemB = hellSystemB;
    }

    public void simpleAction() {
        hellSystemA.actionA();
        hellSystemB.actionBB();
        hellSystemA.actionAAA();
    }
}
