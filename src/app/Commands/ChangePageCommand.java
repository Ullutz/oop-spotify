package app.Commands;

import app.Admin;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class ChangePageCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message = user.changePage(commandInput.getNextPage());

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("message", message);

        return obj;
    }
}
