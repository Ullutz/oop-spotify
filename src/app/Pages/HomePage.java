package app.Pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomePage implements Page {
    private static final int MAX = 5;
    private List<Song> first5LikedSongs = new ArrayList<>();
    private List<Playlist> top5LikedPlaylists = new ArrayList<>();

    /**
     * prints the current page
     *
     * @param user the user
     * @return a string builder
     */
    @Override
    public final StringBuilder printCurrentPage(final User user) {
        updatePage(user);

        StringBuilder message;
        message = new StringBuilder("Liked songs:\n\t[");
        for (Song song : first5LikedSongs) {
            message.append(song.getName()).append(", ");
        }

        if (!first5LikedSongs.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nFollowed playlists:\n\t[");
        for (Playlist playlist : top5LikedPlaylists) {
            message.append(playlist.getName()).append(", ");
        }

        if (!top5LikedPlaylists.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]");

        return message;
    }

    /**
     * updates the page of an user
     *
     * @param user the user
     */
    @Override
    public final void updatePage(final User user) {
        first5LikedSongs.clear();
        List<Song> sortedList = new ArrayList<>(user.getLikedSongs());
        sortedList.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song o1, final Song o2) {
                return o2.getLikes() - o1.getLikes();
            }
        });
        for (int i = 0; i < MAX && i < user.getLikedSongs().size(); i++) {
            first5LikedSongs.add(sortedList.get(i));
        }

        top5LikedPlaylists.clear();

        List<Playlist> sortedPlaylists = new ArrayList<>(user.getFollowedPlaylists());

        sortedPlaylists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist o1, final Playlist o2) {
                int a = o1.calculateTotalLikes();
                int b = o2.calculateTotalLikes();

                return b - a;
            }
        });
        for (int i = 0; i < MAX && i < user.getFollowedPlaylists().size(); i++) {
            top5LikedPlaylists.add(sortedPlaylists.get(i));
        }
    }

    public final List<Song> getFirst5LikedSongs() {
        return first5LikedSongs;
    }

    public final void setFirst5LikedSongs(final List<Song> first5LikedSongs) {
        this.first5LikedSongs = first5LikedSongs;
    }

    public final List<Playlist> getTop5LikedPlaylists() {
        return top5LikedPlaylists;
    }

    public final void setTop5LikedPlaylists(final List<Playlist> top5LikedPlaylists) {
        this.top5LikedPlaylists = top5LikedPlaylists;
    }

    public final String getOwner() {
        return null;
    }

    /**
     * sets the owner of the page
     *
     * @param username the username
     */
    public final void setOwner(final String username) { }
}
