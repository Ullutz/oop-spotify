package app.Pages;

import app.Pages.Visitor.Visitor;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class HomePage implements Page {
    private static final int MAX = 5;
    private List<Song> first5LikedSongs = new ArrayList<>();
    private List<Playlist> top5LikedPlaylists = new ArrayList<>();

    @Override
    public final StringBuilder accept(final Visitor visitor, final User user) {
        return visitor.visit(this, user);
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
