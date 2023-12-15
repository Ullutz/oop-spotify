package app.Commands;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.user.ArtistUser;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

public final class ShowAlbumCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        class AlbumResult {
            private final String name;
            private final List<String> songs;

            AlbumResult(final String name, final List<String> songNames) {
                this.name = name;
                this.songs = songNames;
            }

            public String getName() {
                return name;
            }

            public List<String> getSongs() {
                return songs;
            }
        }

        List<AlbumResult> result = new ArrayList<>();

       for (Album album : ((ArtistUser) user).getAlbums()) {
           List<String> songNames = new ArrayList<>();

           for (Song song : album.getSongs()) {
               songNames.add(song.getName());
           }

           result.add(new AlbumResult(album.getName(), songNames));
       }

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("result", objectMapper.valueToTree(result));

        return obj;
    }
}
