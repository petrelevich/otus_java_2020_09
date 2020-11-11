package ru.outs.visitor;

/**
 * @author sergey
 * created on 12.09.18.
 */
public class Transmission implements Element {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    String refreshOil() {
        return "Заменить масло";
    }
}
