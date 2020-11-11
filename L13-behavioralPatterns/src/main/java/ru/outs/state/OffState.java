package ru.outs.state;

/**
 * @author sergey
 * created on 12.09.18.
 */
public class OffState implements State {
    @Override
    public State action() {
        System.out.println("не светит");
        return StateProvider.getOnState();
    }
}
