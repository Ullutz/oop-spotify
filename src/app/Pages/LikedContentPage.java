package app.Pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.NormalUser;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage implements Page {
    private List<Song> likedSongs = new ArrayList<>();
    private List<Playlist> followedPlaylists = new ArrayList<>();

    @Override
    public StringBuilder printCurrentPage(User user) {
        updatePage(user);

        StringBuilder message = new StringBuilder("Liked songs:\n\t[");
        for (Song song : likedSongs) {
            message.append(song.getName()).append(" - ").
                    append(song.getArtist()).append(", ");
        }

        if (!likedSongs.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nFollowed playlists:\n\t[");
        for (Playlist playlist : followedPlaylists) {
            message.append(playlist.getName()).append(" - ").
                    append(playlist.getOwner()).append(", ");
        }

        if (!followedPlaylists.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]");

        return message;
    }

    @Override
    public void updatePage(User user) {
        likedSongs.clear();
        likedSongs.addAll(((NormalUser) user).getLikedSongs());

        followedPlaylists.clear();
        followedPlaylists.addAll(((NormalUser) user).getFollowedPlaylists());
    }

    public String getOwner() {return null;}

    public void setOwner(String username) {}
}
