package ru.outs.memento;

import java.time.LocalDateTime;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class Demo {
    public static void main(String[] args) {
        Originator originator = new Originator(LocalDateTime::now);

        State abc = new State(new String[]{"A", "B", "C"});
        System.out.println(abc);

        originator.saveState(abc);
        abc.getArray()[0] = "A1";
        System.out.println(abc);

        originator.saveState(abc);
        abc.getArray()[0] = "A2";
        System.out.println(abc);

        originator.saveState(abc);
        abc.getArray()[0] = "A3";
        System.out.println(abc);

        System.out.println("  undo changes");

        abc = originator.restoreState();
        System.out.println(abc);

        abc = originator.restoreState();
        System.out.println(abc);

        abc = originator.restoreState();
        System.out.println(abc);
    }
}
