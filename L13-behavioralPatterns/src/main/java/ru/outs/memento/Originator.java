package ru.outs.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author sergey
 * created on 11.09.18.
 */
class Originator {
    //Фактически, это stack
    private final Deque<Memento> stack = new ArrayDeque<>();

    private final DateTimeProvider dateTimeProvider;

    public Originator(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    void saveState(State state) {
        stack.push(new Memento(state, dateTimeProvider.getDate()));
    }

    State restoreState() {
        var memento = stack.pop();
        System.out.println("createdAt:" + memento.getCreatedAt());
        return memento.getState();
    }
}
