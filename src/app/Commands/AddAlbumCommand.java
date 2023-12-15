package app.Commands;

import app.Admin;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class AddAlbumCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addAlbum(commandInput.getName(), commandInput.getReleaseYear(),
                    commandInput.getDescription(), commandInput.getSongs(),
                    commandInput.getTimestamp());

            Admin.getInstance().addSongs(commandInput.getSongs());
        }

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("message", message);
        return obj;
    }
}
