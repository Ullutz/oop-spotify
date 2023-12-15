package app.user;

import app.Pages.Page;
import app.audio.Collections.*;
import app.audio.Files.Song;
import app.player.Player;
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
        pages = new Page[2 + 2];
        indexOfCurrentPage = 0;
    }

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String switchConnectionStatus();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String addAlbum(String name, int releaseYear,
                                    String desciption, List<SongInput> songs,
                                    int timestamp);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String changePage(String page);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String printCurrentPage();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract List<Song> getLikedSongs();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract List<Playlist> getFollowedPlaylists();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String addEvent(String name, String description, String date);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String removeEvent(String name);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public boolean matchesName(final String name) {
        return false;
    }

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract ArrayList<Album> getAlbums();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract ArrayList<Merch> getMerches();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract ArrayList<Event> getEvents();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String removeAlbum(String name);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String addMerch(String name, String description, int price);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String addAnnouncement(String name, String description);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String removeAnnouncement(String name);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String addPodcast(String name, List<EpisodeInput> episodes);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String removePodcast(String name);

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract ArrayList<Podcast> getPodcasts();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract ArrayList<Announcement> getAnnouncements();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String checkIfUserCanBeDeleted();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract String deleteUsersConnections();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract List<Playlist> getPlaylists();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public abstract void calculateTotalLikes();

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public Player getPlayer() {
        return null;
    }

    /**
     * abstract method ment to be called by this class extenders, so
     * it can act different for avery one of them
     */
    public int getTotalLikes() {
        return 0;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String username) {
        this.username = username;
    }

    public final int getAge() {
        return age;
    }

    public final void setAge(final int age) {
        this.age = age;
    }

    public final String getCity() {
        return city;
    }

    public final void setCity(final String city) {
        this.city = city;
    }

    public final Page[] getPages() {
        return pages;
    }

    public final void setPages(final Page[] pages) {
        this.pages = pages;
    }

    public final int getIndexOfCurrentPage() {
        return indexOfCurrentPage;
    }

    public final void setIndexOfCurrentPage(final int indexOfCurrentPage) {
        this.indexOfCurrentPage = indexOfCurrentPage;
    }
}
