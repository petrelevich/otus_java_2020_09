package ru.outs.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey
 * created on 11.09.18.
 */
class Executor {
    private final List<Command> commands = new ArrayList<>();

    void addCommand(Command command) {
        commands.add(command);
    }

    void executeCommands() {
        commands.stream().map(cmd -> cmd.execute("value"))
                .forEach(System.out::println);
    }
}
