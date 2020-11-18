package ru.otus.adapter;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class SDSadapter implements SDSdril {
    private final Drill drill;

    public SDSadapter(Drill drill) {
        this.drill = drill;
    }

    @Override
    public void action() {
        System.out.println(this.drill);
    }
}
