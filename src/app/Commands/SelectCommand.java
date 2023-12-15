package app.Commands;

import app.Admin;
import app.user.NormalUser;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class SelectCommand implements Command {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message;

        if (!((NormalUser) user).isConnectionStatus()) {
            message = user.getUsername() + " is offline.";
        } else {
            message = ((NormalUser) user).selectHelper(commandInput.getItemNumber());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
}
