package ru.outs.command;

/**
 * @author sergey
 * created on 11.09.18.
 */

@FunctionalInterface
public interface Command {
    String execute(String data);
}
