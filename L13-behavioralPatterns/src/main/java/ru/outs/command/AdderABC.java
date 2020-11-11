package ru.outs.command;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class AdderABC implements Command {
    @Override
    public String execute(String data) {
        return data + "ABC";
    }
}
