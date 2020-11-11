package ru.outs.visitor;

/**
 * @author sergey
 * created on 12.09.18.
 */
public interface Element {
    void accept(Visitor v);
}
