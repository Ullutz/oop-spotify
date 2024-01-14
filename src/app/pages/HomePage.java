package app.pages;

import app.pages.Visitor.Visitor;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

import static app.utils.Enums.HOME_PAGE;

public class HomePage implements Page {
    private final int TYPE = HOME_PAGE;
    private static final int MAX = 5;
    private List<Song> first5LikedSongs = new ArrayList<>();
    private List<Playlist> top5LikedPlaylists = new ArrayList<>();
    private List<Song> songRecommendations = new ArrayList<>();
    private List<Playlist> playlistRecommendations = new ArrayList<>();

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

    public List<Song> getSongRecommendations() {
        return songRecommendations;
    }

    public void setSongRecommendations(List<Song> songRecommendations) {
        this.songRecommendations = songRecommendations;
    }

    public List<Playlist> getPlaylistRecommendations() {
        return playlistRecommendations;
    }

    public void setPlaylistRecommendations(List<Playlist> playlistRecommendations) {
        this.playlistRecommendations = playlistRecommendations;
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

    public final String getType() {
        return "Home";
    }
}
