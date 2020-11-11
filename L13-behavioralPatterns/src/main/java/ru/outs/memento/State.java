package ru.outs.memento;

import java.util.Arrays;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class State {
    private final String[] array;

    State(String[] array) {
        this.array = array;
    }

    State(State state) {
        //Не забывайте про "глубокую" копию
        this.array = Arrays.copyOf(state.getArray(), state.getArray().length);
    }

    String[] getArray() {
        return array;
    }

    @Override
    public String toString() {
        return "State{" +
                "array=" + (array == null ? null : Arrays.asList(array)) +
                '}';
    }
}
