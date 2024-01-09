package app.commands;

import app.Admin;
import app.notification.Notifications;
import app.user.NotificationsObserver;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

public class GetNotificastionsCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ArrayList<Notifications> notifications = user.getNotifications();

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("notifications", objectMapper.valueToTree(notifications));

        return obj;
    }
}
