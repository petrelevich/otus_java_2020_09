package ru.outs.state;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class Demo {
    public static void main(String[] args) {

        var context = new BulbContext();
        context.performAction();

        context.performAction();

        context.performAction();
    }
}
