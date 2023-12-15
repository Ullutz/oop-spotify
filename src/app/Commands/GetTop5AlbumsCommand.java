package app.Commands;

import app.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.List;

public final class GetTop5AlbumsCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        List<String> results = Admin.getInstance().getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(results));

        return objectNode;
    }
}
