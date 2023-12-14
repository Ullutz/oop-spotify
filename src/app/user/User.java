package app.user;

import app.Pages.Page;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.player.PodcastBookmark;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
public abstract class User {
    private String username;
    private int age;
    private String city;
    protected Page[] pages;
    protected int indexOfCurrentPage;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        pages = new Page[4];
        indexOfCurrentPage = 0;
    }

    public abstract String switchConnectionStatus();
    public abstract String addAlbum(final String name, final int releaseYear,
                                    final String desciption, final List<SongInput> songs,
                                    final int timestamp);
    public abstract String changePage(String page);
    public abstract String printCurrentPage();
    public abstract List<Song> getLikedSongs();
    public abstract List<Playlist> getFollowedPlaylists();
    public abstract String addEvent(String name, String description, String date);
    public abstract String removeEvent(String name);
    public boolean matchesName(String name) {return false;}
    public abstract ArrayList<Album> getAlbums();
    public abstract ArrayList<Merch> getMerches();
    public abstract ArrayList<Event> getEvents();
    public abstract String removeAlbum(String name);
    public abstract String addMerch(String name, String description, int price);
    public abstract String addAnnouncement(String name, String description);
    public abstract String removeAnnouncement(String name);
    public abstract String addPodcast(String name, List<EpisodeInput> episodes);
    public abstract String removePodcast(String name);
    public abstract ArrayList<Podcast> getPodcasts();
    public abstract ArrayList<Announcement> getAnnouncements();
    public abstract String checkIfUserCanBeDeleted();
    public abstract String deleteUsersConnections();
    public abstract List<Playlist> getPlaylists();
    public abstract void calculateTotalLikes();
    public Player getPlayer() {return null;}

    public int getTotalLikes() {
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Page[] getPages() {
        return pages;
    }

    public void setPages(Page[] pages) {
        this.pages = pages;
    }

    public int getIndexOfCurrentPage() {
        return indexOfCurrentPage;
    }

    public void setIndexOfCurrentPage(int indexOfCurrentPage) {
        this.indexOfCurrentPage = indexOfCurrentPage;
    }
}
