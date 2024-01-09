package app.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public interface Command {

    /**
     * executes a command
     * @param commandInput the input of that command
     * @return an objectnode
     */
    ObjectNode execute(CommandInput commandInput);
}
