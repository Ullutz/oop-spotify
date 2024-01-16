package app.commands;

import app.Admin;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;

public class WrappedCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(CommandInput commandInput) {
        ObjectNode obj = objectMapper.createObjectNode();
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            obj.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return obj;
        }

        ObjectNode result = objectMapper.createObjectNode();

        result = user.getWrapped();

        if (result == null) {
            obj.put("message", user.getWrappedErrorMessage());
            return obj;
        }

        obj.put("result", result);
        return obj;
    }
}
