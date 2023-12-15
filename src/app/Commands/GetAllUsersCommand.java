package app.Commands;

import app.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.List;

public final class GetAllUsersCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        List<String> result = Admin.getInstance().getAllUsers();

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("result", objectMapper.valueToTree(result));

        return obj;
    }
}
