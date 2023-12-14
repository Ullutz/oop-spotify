package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.ArtistUser;
import app.user.HostUser;
import app.user.NormalUser;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.*;

/**
 * The type Admin.
 */
public final class Admin {
    private static Admin instance = null;
    private List<User> users = new ArrayList<>();
    private int noNormalUsers = 0;
    private int noArtistUsers = 0;
    private int noHostUsers = 0;
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private int timestamp = 0;
    private final int LIMIT = 5;

    private Admin() {
    }

    public static Admin getInstance() {
        if (instance == null)
            instance = new Admin();

        return instance;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUser(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new NormalUser(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
            noNormalUsers++;
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSong(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Adds songs when an album is added
     *
     * @param songs the songs that need to be added
     */
    public void addSongs(final List<SongInput> songs) {
        for (SongInput songInput : songs) {
            Song newSong = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());

            if (verifyIfSongAlreadyExists(newSong))
                this.songs.add(newSong);
        }
    }

    /**
     * Verifies if a song already exists in the library
     *
     * @param songToBePlaced the song that is verified
     * @return 1 if the song doesn't exist already, 0 otherwise
     *
     */
    public boolean verifyIfSongAlreadyExists(Song songToBePlaced) {
        Set<Song> uniqueElements = new HashSet<>(songs);

        return uniqueElements.add(songToBePlaced);
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcast(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (int i = 0; i < noNormalUsers; i++) {
            playlists.addAll(((NormalUser) users.get(i)).getPlaylists());
        }

        return playlists;
    }

    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            albums.addAll(((ArtistUser) users.get(i)).getAlbums());
        }

        return albums;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (int i = 0; i < noNormalUsers; i++) {
            if (((NormalUser) users.get(i)).isConnectionStatus())
                ((NormalUser) users.get(i)).simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public List<String> getTop5Artists() {
        List<User> sortedUsers = new ArrayList<>();
        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            users.get(i).calculateTotalLikes();
            sortedUsers.add(users.get(i));
        }

        sortedUsers.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getTotalLikes() - o1.getTotalLikes();
            }
        });

        List<String> result = new ArrayList<>();

        for (User user : sortedUsers) {
            if (result.size() < LIMIT) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * Gets all the online users
     *
     * @return the usernames of the online users
     */
    public List<String> getOnlineUsers() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < noNormalUsers; i++) {
            if (((NormalUser) users.get(i)).isConnectionStatus())
                result.add(users.get(i).getUsername());
        }

        return result;
    }

    /**
     * Gets all the users
     *
     * @return the useranames of every user
     */
    public List<String> getAllUsers() {
        List<String> result = new ArrayList<>();

        for (User user : users) {
            result.add(user.getUsername());
        }

        return result;
    }

    /**
     * Gets all the artists
     *
     * @return a list of all the artists
     */
    public List<ArtistUser> getArtists() {
        List<ArtistUser> artistUsers = new ArrayList<>();

        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            artistUsers.add((ArtistUser) users.get(i));
        }

        return artistUsers;
    }

    public List<HostUser> getHosts() {
        List<HostUser> hostUsers = new ArrayList<>();

        for (int i = noNormalUsers + noArtistUsers; i < users.size(); i++) {
            hostUsers.add((HostUser) users.get(i));
        }

        return hostUsers;
    }

    public String addUser(final String type, final String username,
                          final int age, final String city) {

        if (getUser(username) != null) {
            return "The username " + username + " is already taken.";
        }

        switch (type) {
            case "user" -> {
                NormalUser user = new NormalUser(username, age, city);

                users.add(noNormalUsers, user);
                noNormalUsers++;
            }
            case "artist" -> {
                ArtistUser user = new ArtistUser(username, age, city);

                users.add(noNormalUsers + noArtistUsers, user);
                noArtistUsers++;
            }
            case "host" -> {
                HostUser user = new HostUser(username, age, city);

                users.add(users.size(), user);
                noHostUsers++;
            }
        }

        return "The username " + username + " has been added successfully.";
    }

    /**
     * decides which type of user needs to be deleted
     *
     * @param username the username
     * @return message
     */
    public String deleteUserHelper(final String username) {
        User user = getUser(username);

        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }

        int indexOfUser = users.indexOf(user);
        String message = user.checkIfUserCanBeDeleted();

        if (message.equals("ok")) {
            message = user.deleteUsersConnections();
            users.remove(user);
        }

        return message;
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
        noNormalUsers = 0;
        noArtistUsers = 0;
        noHostUsers = 0;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        noNormalUsers = users.size();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public int getNoArtistUsers() {
        return noArtistUsers;
    }

    public void setNoArtistUsers(int noArtistUsers) {
        this.noArtistUsers = noArtistUsers;
    }

    public int getNoHostUsers() {
        return noHostUsers;
    }

    public void setNoHostUsers(int noHostUsers) {
        this.noHostUsers = noHostUsers;
    }

    public int getNoNormalUsers() {
        return noNormalUsers;
    }

    public void setNoNormalUsers(int noNormalUsers) {
        this.noNormalUsers = noNormalUsers;
    }
}
