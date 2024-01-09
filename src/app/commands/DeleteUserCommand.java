package app.commands;

import app.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class DeleteUserCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
       String message = Admin.getInstance().deleteUserHelper(commandInput.getUsername());

       ObjectNode obj = objectMapper.createObjectNode();
       obj.put("command", commandInput.getCommand());
       obj.put("user", commandInput.getUsername());
       obj.put("timestamp", commandInput.getTimestamp());
       obj.put("message", message);

       return obj;
    }
}
