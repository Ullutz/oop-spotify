package app.Pages;

import app.Pages.Visitor.Visitor;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage implements Page {
    private List<Song> likedSongs = new ArrayList<>();
    private List<Playlist> followedPlaylists = new ArrayList<>();

    @Override
    public final StringBuilder accept(final Visitor visitor, final User user) {
        return visitor.visit(this, user);
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

    public final List<Song> getLikedSongs() {
        return likedSongs;
    }

    public final void setLikedSongs(final List<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public final List<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public final void setFollowedPlaylists(final List<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }
}
