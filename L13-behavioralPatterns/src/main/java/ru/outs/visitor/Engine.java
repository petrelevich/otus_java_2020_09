package ru.outs.visitor;

/**
 * @author sergey
 * created on 12.09.18.
 */
public class Engine implements Element {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    String checkEngine() {
        return "Engine Ok";
    }
}
