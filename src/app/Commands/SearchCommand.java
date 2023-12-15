package app.Commands;

import app.Admin;
import app.searchBar.Filters;
import app.user.NormalUser;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class SearchCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        String message;
        ArrayList<String> results = new ArrayList<>();

        if (!((NormalUser) user).isConnectionStatus()) {
            message = user.getUsername() + " is offline.";
        } else {
            results = ((NormalUser) user).search(filters, type);

            message = "Search returned " + results.size() + " results";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }
}
