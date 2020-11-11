package ru.outs.visitor;

/**
 * @author sergey
 * created on 12.09.18.
 */
public interface Visitor {
    void visit(Engine item);

    void visit(Transmission item);

    void visit(Brake item);
}
