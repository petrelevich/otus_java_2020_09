package ru.outs.state;

/**
 * @author sergey
 * created on 12.09.18.
 */
public class OnState implements State {
    @Override
    public State action() {
        System.out.println("лампа светит");
        return StateProvider.getOffState();
    }
}
