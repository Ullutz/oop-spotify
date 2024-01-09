package app.commands;

import app.Admin;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public class SeeMyMerchCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode obj = objectMapper.createObjectNode();

        obj.put("command", commandInput.getCommand());
        obj.put("username", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            obj.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            obj.put("result", objectMapper.valueToTree(user.seeMyMerch()));
        }

         return obj;
    }
}
