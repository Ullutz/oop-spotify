package app.user;

import app.Admin;
import app.Pages.Page;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Song;
import app.player.Player;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import javax.management.StandardEmitterMBean;
import java.util.*;

public class ArtistUser extends User {

    private ArrayList<Album> albums;
    private ArrayList<Merch> merches;
    private ArrayList<Event> events;


    public ArtistUser(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merches = new ArrayList<>();
        events = new ArrayList<>();
    }

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
    public String addAlbum(String name, int releaseYear,
                           String description, List<SongInput> songs, final int timestamp) {
        for(Album album : albums) {
            if (album.getName().equals(name))
                return this.getUsername() + " has another album with the same name.";
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
                    if (album.getSongs().contains(song))
                        return getUsername() + " can't delete this album.";
                }
            }
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            Player player = ((NormalUser) Admin.getInstance().
                    getUsers().get(i)).getPlayer();

            if (player.getSource().getAudioCollection().equals(album)) {
                return getUsername() + " can't delete this album.";
            }
        }

        albums.remove(album);
        for (Song song : album.getSongs()) {
            Admin.getInstance().getSongs().remove(song);
        }

        return getUsername() + " deleted the album successfully.";
    }


    @Override
    public String addEvent(final String name, final String description, final String date) {

        for (Event event : events) {
            if (event.getName().equals(name))
                return getUsername() + " has another event with the same name.";
        }

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year =  Integer.parseInt(date.substring(6, 10));

        if (day > 31 || month > 12 || (month == 12 && day > 28) || year > 2023 || year < 1990)
            return "Event for " + getUsername() + " does not have a valid date.";

        events.add(new Event(name, description, date));

        return getUsername() + " has added new event successfully.";
    }

    @Override
    public String removeEvent(final String name) {
        int found = 0;
        for (Event event : events) {
            if (event.getName().equals(name)) {
                found = 1;
                break;
            }
        }

        if (found == 0)
            return getUsername() + " doesn't have an event with the given name.";

        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)){
                iterator.remove();
            }
        }

        return getUsername() + " deleted the event successfully.";
    }

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

    @Override
    public List<Playlist> getPlaylists() {
        return null;
    }

    @Override
    public String addAnnouncement(String name, String description) {
        return getUsername() + " is not a host.";
    }

    @Override
    public String removeAnnouncement(String name) {
        return getUsername() + " is not a host.";
    }

    @Override
    public String addPodcast(String name, List<EpisodeInput> episodes) {
        return getUsername() + " is not a host.";
    }

    @Override
    public String removePodcast(String name) {
        return getUsername() + " is not a host.";
    }

    @Override
    public ArrayList<Podcast> getPodcasts() {
        return null;
    }

    @Override
    public ArrayList<Announcement> getAnnouncements() {
        return null;
    }

    @Override
    public boolean matchesName(String name) {
        return getUsername().startsWith(name);
    }

    @Override
    public String printCurrentPage() {
        return getUsername() + " is not a normal user.";
    }

    @Override
    public List<Song> getLikedSongs() {
        return null;
    }

    @Override
    public List<Playlist> getFollowedPlaylists() {
        return null;
    }

    @Override
    public String changePage(String page) {
        return getUsername() + " is not a normal user.";
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<Merch> getMerches() {
        return merches;
    }

    public void setMerches(ArrayList<Merch> merches) {
        this.merches = merches;
    }

    @Override
    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
