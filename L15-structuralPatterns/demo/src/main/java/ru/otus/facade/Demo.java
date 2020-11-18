package ru.otus.facade;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class Demo {
    public static void main(String[] args) {
        var systemA = new HellSystemA();
        var systemB = new HellSystemB();

        var facade = new Facade(systemA, systemB);
        facade.simpleAction();
    }
}
