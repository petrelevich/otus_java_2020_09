package ru.outs.visitor;

/**
 * @author sergey
 * created on 12.09.18.
 */
public class Brake implements Element {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    String replaceBrakePad() {
        return "Заменить тормозные колодки";
    }
}
