package app.Commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public interface Command {

    public ObjectNode execute(final CommandInput commandInput);
}
