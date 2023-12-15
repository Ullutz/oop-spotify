package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Song;
import app.player.Player;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.*;

import static app.utils.Enums.*;

public class ArtistUser extends User {

    private int totalLikes;
    private ArrayList<Album> albums;
    private ArrayList<Merch> merches;
    private ArrayList<Event> events;


    public ArtistUser(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merches = new ArrayList<>();
        events = new ArrayList<>();
        totalLikes = 0;
    }

    /**
     * @return an error message since an artist can't be online or offline
     */
    @Override
    public String switchConnectionStatus() {
        return this.getUsername() + " is not a normal user.";
    }

    /**
     * Adds an album to the list of albums
     *
     * @param name the name of the album
     * @param releaseYear the realease year
     * @param description the description
     * @param songs the list of songs that is added
     * @return message
     */
    @Override
    public String addAlbum(final String name, final int releaseYear,
                           final String description, final List<SongInput> songs,
                           final int timestamp) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return this.getUsername() + " has another album with the same name.";
            }
        }

        Set<String> uniqueSongs = new HashSet<>();
        for (SongInput song : songs) {
            if (!uniqueSongs.add(song.getName())) {
                return this.getUsername() + " has the same song at least twice in this album.";
            }
        }

        Album album = new Album(name, this.getUsername(), description, releaseYear, timestamp);

        for (SongInput songInput : songs) {
            Song song = new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(), songInput.getTags(), songInput.getLyrics(),
                    songInput.getGenre(), songInput.getReleaseYear(), songInput.getArtist());
            album.getSongs().add(song);
        }

        albums.add(album);

        return this.getUsername() + " has added new album successfully.";
    }

    /**
     * removes an album
     *
     * @param albumName the name
     * @return a message
     */
    @Override
    public String removeAlbum(final String albumName) {
        Album album = null;
        for (Album a : albums) {
            if (a.getName().equals(albumName)) {
                album = a;
                break;
            }
        }

        if (album == null) {
            return getUsername() + " doesn't have an album with the given name.";
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            for (Playlist playlist : ((NormalUser) Admin.getInstance().
                    getUsers().get(i)).getPlaylists()) {
                for (Song song : playlist.getSongs()) {
                    if (album.getSongs().contains(song)) {
                        return getUsername() + " can't delete this album.";
                    }
                }
            }
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            Player player = ((NormalUser) Admin.getInstance().
                    getUsers().get(i)).getPlayer();

            if (player.getSource() != null) {
                if (player.getSource().getAudioCollection().equals(album)) {
                    return getUsername() + " can't delete this album.";
                }
            }
        }

        albums.remove(album);
        for (Song song : album.getSongs()) {
            Admin.getInstance().getSongs().remove(song);
        }

        return getUsername() + " deleted the album successfully.";
    }


    /**
     * adds an event
     *
     * @param name the name of the event
     * @param description the description of the event
     * @param date the date of the event
     * @return a message
     */
    @Override
    public String addEvent(final String name, final String description, final String date) {

        for (Event event : events) {
            if (event.getName().equals(name)) {
                return getUsername() + " has another event with the same name.";
            }
        }

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(THREE, FIVE));
        int year =  Integer.parseInt(date.substring(SIX, TEN));

        if (day > MAXDAY || month > MAXMOUTH
                || (month == 2 && day > MAXDAYFORFEB) || year > MAXYEAR || year < MINYEAR) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        events.add(new Event(name, description, date));

        return getUsername() + " has added new event successfully.";
    }

    /**
     * removes and event
     *
     * @param name the name of the event
     * @return a message
     */
    @Override
    public String removeEvent(final String name) {
        int found = 0;
        for (Event event : events) {
            if (event.getName().equals(name)) {
                found = 1;
                break;
            }
        }

        if (found == 0) {
            return getUsername() + " doesn't have an event with the given name.";
        }

        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                iterator.remove();
            }
        }

        return getUsername() + " deleted the event successfully.";
    }

    /**
     * adds merch
     *
     * @param name the name of the merch
     * @param description the description of the merch
     * @param price the price of the merch
     * @return a message
     */
    @Override
    public String addMerch(final String name, final String description, final int price) {
        for (Merch merch : merches) {
            if (merch.getName().equals(name)) {
                return getUsername() + " has merchandise with the same name.";
            }
        }

        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        Merch merch = new Merch(name, description, price);
        merches.add(merch);
        return getUsername() + " has added new merchandise successfully.";
    }

    /**
     * checks if the artist can be deleted
     *
     * @return a message
     */
    @Override
    public String checkIfUserCanBeDeleted() {
        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
                    User user = Admin.getInstance().getUsers().get(i);
                    Player player = user.getPlayer();

                    if (player.getCurrentAudioFile() != null) {
                        if (song.getName().equals(player.getCurrentAudioFile().getName())) {
                            return getUsername() + " can't be deleted.";
                        }
                    }
                }
            }
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            User user = Admin.getInstance().getUsers().get(i);
            int indexOfPage = user.getIndexOfCurrentPage();

            if (indexOfPage == 2) {
                if (user.getPages()[2].getOwner().equals(getUsername())) {
                    return getUsername() + " can't be deleted.";
                }
            }
        }

        return "ok";
    }

    /**
     * deletes an artist and all of its connections
     *
     * @return a message
     */
    @Override
    public String deleteUsersConnections() {
        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            User user = Admin.getInstance().getUsers().get(i);

            for (Album album : albums) {
                for (Song song : album.getSongs()) {
                    user.getLikedSongs().remove(song);
                }
            }
        }

        Iterator<Album> iterator = albums.iterator();

        while (iterator.hasNext()) {
            for (Song song : iterator.next().getSongs()) {
                Admin.getInstance().getSongs().remove(song);
            }
            iterator.remove();
        }

        Admin.getInstance().setNoArtistUsers(Admin.getInstance().getNoArtistUsers() - 1);

        return getUsername() + " was successfully deleted.";
    }

    /**
     * calculates the total likes of an album
     */
    @Override
    public void calculateTotalLikes() {
        totalLikes = 0;
        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                totalLikes += song.getLikes();
            }
        }
    }

    /**
     * @return null since an artist doesn't have playlists
     */
    @Override
    public List<Playlist> getPlaylists() {
        return null;
    }

    /**
     * @return an error message since an artist can't add announcements
     */
    @Override
    public String addAnnouncement(final String name, final String description) {
        return getUsername() + " is not a host.";
    }

    /**
     * @return an error message since an artist can't remove an announcement
     */
    @Override
    public String removeAnnouncement(final String name) {
        return getUsername() + " is not a host.";
    }

    /**
     * @return an error message since an artist can't add a podcast
     */
    @Override
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        return getUsername() + " is not a host.";
    }

    /**
     * @return an error message since an artist can't remove a podcast
     */
    @Override
    public String removePodcast(final String name) {
        return getUsername() + " is not a host.";
    }

    /**
     * @return null since an artist doesn't have podcasts
     */
    @Override
    public ArrayList<Podcast> getPodcasts() {
        return null;
    }

    /**
     * @return null since an artist doesn't have announcements
     */
    @Override
    public ArrayList<Announcement> getAnnouncements() {
        return null;
    }

    /**
     * checks if the artist's name starts with the name given
     *
     * @param name the name
     * @return true or false
     */
    @Override
    public boolean matchesName(final String name) {
        return getUsername().startsWith(name);
    }

    /**
     * @return an error message since an artist can't print a page
     */
    @Override
    public String printCurrentPage() {
        return getUsername() + " is not a normal user.";
    }

    /**
     * @return null since an artist doesn't have liked songs
     */
    @Override
    public List<Song> getLikedSongs() {
        return null;
    }

    /**
     * @return null since an artist doesn't have followed playlists
     */
    @Override
    public List<Playlist> getFollowedPlaylists() {
        return null;
    }

    /**
     * @return an error message since an artist can't chane a page
     */
    @Override
    public String changePage(final String page) {
        return getUsername() + " is not a normal user.";
    }

    public final ArrayList<Album> getAlbums() {
        return albums;
    }

    public final void setAlbums(final ArrayList<Album> albums) {
        this.albums = albums;
    }

    public final ArrayList<Merch> getMerches() {
        return merches;
    }

    public final void setMerches(final ArrayList<Merch> merches) {
        this.merches = merches;
    }

    @Override
    public final ArrayList<Event> getEvents() {
        return events;
    }

    public final void setEvents(final ArrayList<Event> events) {
        this.events = events;
    }

    public final int getTotalLikes() {
        return totalLikes;
    }

    public final void setTotalLikes(final int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
