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
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import javax.swing.border.EmptyBorder;
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
    private final int limit = 5;

    private Admin() {
    }

    /**
     * SingleTon pattern
     *
     * @return the instance of the Admin
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }

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
            users.add(new NormalUser(userInput.getUsername(),
                    userInput.getAge(), userInput.getCity()));
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
            Song newSong = new Song(songInput.getName(),
                    songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());

            if (verifyIfSongAlreadyExists(newSong)) {
                this.songs.add(newSong);
            }
        }
    }

    /**
     * Verifies if a song already exists in the library
     *
     * @param songToBePlaced the song that is verified
     * @return 1 if the song doesn't exist already, 0 otherwise
     *
     */
    public boolean verifyIfSongAlreadyExists(final Song songToBePlaced) {
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

    /**
     * Gets albums
     *
     * @return the albums
     */
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
            if (((NormalUser) users.get(i)).isConnectionStatus()) {
                ((NormalUser) users.get(i)).simulateTime(elapsed);
            }
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
            if (count >= limit) {
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
            if (count >= limit) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets the top 5 Artists
     *
     * @return the top 5 artists
     */
    public List<String> getTop5Artists() {
        List<User> sortedUsers = new ArrayList<>();
        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            users.get(i).calculateTotalLikes();
            sortedUsers.add(users.get(i));
        }

        sortedUsers.sort(new Comparator<User>() {
            @Override
            public int compare(final User o1, final User o2) {
                return o2.getTotalLikes() - o1.getTotalLikes();
            }
        });

        List<String> result = new ArrayList<>();

        for (User user : sortedUsers) {
            if (result.size() < limit) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * Gets the top 5 albums
     *
     * @return the top 5 albums
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        for (Album album : sortedAlbums) {
            album.calculateTotalLikes();
        }
        sortedAlbums.sort(Comparator.comparingInt(Album::getTotalLikes)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedAlbums) {
            if (count >= limit) {
                break;
            }
            topAlbums.add(playlist.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets all the online users
     *
     * @return the usernames of the online users
     */
    public List<String> getOnlineUsers() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < noNormalUsers; i++) {
            if (((NormalUser) users.get(i)).isConnectionStatus()) {
                result.add(users.get(i).getUsername());
            }
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

    /**
     * Gets all the hosts
     *
     * @return a list of all the hosts
     */
    public List<HostUser> getHosts() {
        List<HostUser> hostUsers = new ArrayList<>();

        for (int i = noNormalUsers + noArtistUsers; i < users.size(); i++) {
            hostUsers.add((HostUser) users.get(i));
        }

        return hostUsers;
    }

    /**
     * Adds an user to the list based on its type
     *
     * @param type the type of user
     * @param username the username
     * @param age the age
     * @param city the city
     * @return message
     */
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
            default -> { }
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
     * Gets the song instance in the library of a song
     *
     * @param song the song
     * @return the instance of the song that is in the library
     */
    public Song getSong(final Song song) {
        for (Song song1 : songs) {
            if (song1.equals(song)) {
                return song1;
            }
        }

        return null;
    }

    /**
     * updates the wrapped for the given user
     *
     * @param user the user that is updated
     */
    public void updateUserWrapped(NormalUser user) {
        if (user.getPlayer().getSource() == null) {
            return;
        }

        if (!user.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PODCAST)) {
            Song song = (Song) user.getPlayer().getCurrentAudioFile();
            ArtistUser artistUser = (ArtistUser) getUser(song.getArtist());

            artistUser.incrementSong(song.getName());
            artistUser.incrementAlbum(song.getAlbum());
            artistUser.incrementFan(user.getUsername());
            artistUser.addListeners(user.getUsername());
            artistUser.setListened(true);

            user.incrementSong(song.getName());
            user.incrementGenre(song.getGenre());
            user.incrementArtist(song.getArtist());
            user.incrementAlbums(song.getAlbum());

            if (user.isPremiumSubscription()) {
                if (user.getListenedWhilePremium().containsKey(artistUser)) {
                    user.getListenedWhilePremium().put(artistUser,
                            user.getListenedWhilePremium().get(artistUser) + 1);
                } else {
                    user.getListenedWhilePremium().put(artistUser, 1);
                }

                if (artistUser.getSongListenedByPremium().containsKey(song)) {
                    artistUser.getSongListenedByPremium().put(song,
                            artistUser.getSongListenedByPremium().get(song) + 1);
                } else {
                    artistUser.getSongListenedByPremium().put(song, 1);
                }
            }
        }

        if (user.getPlayer().getSource().getType().equals(Enums.PlayerSourceType.PODCAST)) {
            Episode episode = (Episode) user.getPlayer().getCurrentAudioFile();
            Podcast podcast = (Podcast) user.getPlayer().getSource().getAudioCollection();
            HostUser hostUser = (HostUser) getUser(podcast.getOwner());

            user.incrementEpisodes(episode.getName());

            if (hostUser != null) {
                hostUser.incrementTopEpisodes(episode.getName());
                hostUser.incrementListeners(user.getUsername());
                hostUser.setListened(true);
            }
        }
    }

    public void getMostProfitableSong() {
        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            ArtistUser artistUser = (ArtistUser) users.get(i);
            Song mostProfitableSong = null;
            double maxProfit = -1;

            for (Album album : artistUser.getAlbums()) {
                for (Song song : album.getSongs()) {
                    System.out.println(song.getName() + " " + song.getProfit());
                    if (song.getProfit() > maxProfit && song.getProfit() > 0) {
                        maxProfit = song.getProfit();
                        mostProfitableSong = song;
                    }
                }
            }

            if (mostProfitableSong != null) {
                artistUser.setMostProfitableSong(mostProfitableSong.getName());
            }
        }
    }

    /**
     * calculates the revenue made by premium listeners
     *
     */
    public void calculateSongsRevenuePremium() {
        for (int i = 0; i < noNormalUsers; i++) {
            NormalUser user = (NormalUser) users.get(i);

            double listenedSongs = user.getListenedWhilePremium().values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            for (Map.Entry<ArtistUser, Integer> entry
                    : user.getListenedWhilePremium().entrySet()) {
                ArtistUser artist = entry.getKey();
                double songs = entry.getValue();

                double value = artist.getSongRevenue() + 1000000.00 / listenedSongs * songs;
                artist.setSongRevenue(Math.round(value * 100.00) / 100.00);
            }
        }

        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            ArtistUser artistUser = (ArtistUser) users.get(i);

            double listenedSongs = artistUser.getSongListenedByPremium().values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            for (Map.Entry<Song, Integer> entry
                    : artistUser.getSongListenedByPremium().entrySet()) {
                Song song = entry.getKey();
                double listens = entry.getValue();

                double value = song.getProfit() + 1000000.00 / listenedSongs * listens;
                value = Math.round(value * 100.00) / 100.00;
                song.setProfit(value);
                artistUser.getSong(song.getName()).setProfit(value);
            }
        }
    }

    /**
     * calculates the rankings for all the artists
     *
     * @return the list of sorted artists by the ranking and name
     */
    public List<User> setUpArtistRankings() {
        calculateSongsRevenuePremium();
        getMostProfitableSong();
        List<User> sortedList = new ArrayList<>();

        for (int i = noNormalUsers; i < noNormalUsers + noArtistUsers; i++) {
            if (((ArtistUser) users.get(i)).isListened()
                    || ((ArtistUser) users.get(i)).isSoldMerch()) {
                sortedList.add(users.get(i));
            }
        }

        sortedList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                double revenue1 = ((ArtistUser) o1).getSongRevenue() +
                        ((ArtistUser) o1).getMerchRevenue();

                double revenue2 = ((ArtistUser) o2).getSongRevenue() +
                        ((ArtistUser) o2).getMerchRevenue();

                if (revenue2 - revenue1 > 0)
                    return 1;

                if (revenue2 - revenue1 < 0)
                    return -1;

                return o1.getUsername().compareTo(o2.getUsername());
            }
        });

        for (int i = 0; i < sortedList.size(); i++) {
            ((ArtistUser) sortedList.get(i)).setRanking(i + 1);
        }

        return sortedList;
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

    /**
     * sets the user list
     *
     * @param users the users
     */
    public void setUsers(final List<User> users) {
        this.users = users;
        noNormalUsers = users.size();
    }

    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    public void setPodcasts(final List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public int getLIMIT() {
        return limit;
    }

    public int getNoArtistUsers() {
        return noArtistUsers;
    }

    public void setNoArtistUsers(final int noArtistUsers) {
        this.noArtistUsers = noArtistUsers;
    }

    public int getNoHostUsers() {
        return noHostUsers;
    }

    public void setNoHostUsers(final int noHostUsers) {
        this.noHostUsers = noHostUsers;
    }

    public int getNoNormalUsers() {
        return noNormalUsers;
    }

    public void setNoNormalUsers(final int noNormalUsers) {
        this.noNormalUsers = noNormalUsers;
    }
}
