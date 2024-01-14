package app.user;

import app.Admin;
import app.notification.Notifications;
import app.pages.*;
import app.pages.Visitor.PrintPageVisitor;
import app.pages.Visitor.UpdatePageVisitor;
import app.pages.Visitor.Visitor;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import javax.crypto.spec.PSource;
import java.util.*;
import java.util.stream.Collectors;

import static app.utils.Enums.*;

public class NormalUser extends User {
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private boolean connectionStatus;
    private HashMap<String, Integer> topArtists;
    private HashMap<String, Integer> topGenres;
    private HashMap<String, Integer> topSongs;
    private HashMap<String, Integer> topAlbums;
    private HashMap<String, Integer> topEpisodes;
    private ArrayList<String> boughtMerch;
    private boolean premiumSubscription;
    private ArrayList<Notifications> notificationBar;
    private ArrayList<Page> pageHistory;
    private int pageHistoryIndex;
    private Song recommendedSong;
    private Playlist recommendedPlaylist;
    private RecommendationType recommedation;
    private HashMap<ArtistUser, Integer> listenedWhilePremium;

    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        connectionStatus = true;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        pages[HOME_PAGE] = new HomePage();
        pages[LIKED_CONTENT_PAGE] = new LikedContentPage();
        pages[ARTIST_PAGE] = new ArtistPage();
        pages[HOST_PAGE] = new HostPage();
        topArtists = new HashMap<>();
        topGenres = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topEpisodes = new HashMap<>();
        boughtMerch = new ArrayList<>();
        premiumSubscription = false;
        notificationBar = new ArrayList<>();
        pageHistory = new ArrayList<>();
        pageHistory.add(pages[0]);
        pageHistoryIndex = 0;
        recommedation = RecommendationType.NONE;
        listenedWhilePremium = new HashMap<>();
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        if (type.equals("artist") || type.equals("host")) {
            List<User> userEntries = searchBar.searchArtistsOrHosts(filters, type);
            for (User user : userEntries) {
                results.add(user.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.searchAudio(filters, type);
            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Selected helper, it decides which type of select it runs
     *
     * @param itemNumber the item number
     * @return message
     */
    public String selectHelper(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist")
                || searchBar.getLastSearchType().equals("host")) {
            return selectArtistOrHost(itemNumber);
        } else {
            return selectAudio(itemNumber);
        }
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String selectAudio(final int itemNumber) {

        LibraryEntry selectedAudio = searchBar.selectAudio(itemNumber);

        if (selectedAudio == null) {
            return "The selected ID is too high.";
        }

        return "Successfully selected %s.".formatted(selectedAudio.getName());
    }

    /**
     * Select string
     *
     * @param itemNumber the item number
     * @return the message
     */
    public String selectArtistOrHost(final int itemNumber) {

        User selectedUser = searchBar.selectUser(itemNumber);

        if (selectedUser == null) {
            return "The selected ID is too high.";
        }

        if (searchBar.getLastSearchType().equals("artist")) {
            pages[2].setOwner(selectedUser.getUsername());
            changePage("Artist");
        } else {
            pages[2 + 1].setOwner(selectedUser.getUsername());
            changePage("Host");
        }

        return "Successfully selected %s's page.".formatted(selectedUser.getUsername());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelectedAudio() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelectedAudio()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelectedAudio(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        if (premiumSubscription) {
            if (!player.getSource().getType().equals(PlayerSourceType.PODCAST)) {
                ArtistUser artist = (ArtistUser) Admin.getInstance()
                        .getUser(((Song) player.getCurrentAudioFile()).getArtist());
                if (listenedWhilePremium.containsKey(artist)) {
                    listenedWhilePremium.put(artist, listenedWhilePremium.get(artist) + 1);
                } else {
                    listenedWhilePremium.put(artist, 1);
                }
            }
        }
        Admin.getInstance().updateUserWrapped(this);
        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
        && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        if (!connectionStatus) {
            return getUsername() + " is offline.";
        }

        Song song = (Song) player.getCurrentAudioFile();
        Song songEntry = Admin.getInstance().getSong(song);

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            songEntry.setLikes(song.getLikes());

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        songEntry.setLikes(song.getLikes());
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelectedAudio();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);

        Admin.getInstance().getUser(playlist.getOwner()).update("New follower", "New follower");

        followedPlaylists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist o1, final Playlist o2) {
                return o2.calculateTotalLikes() - o1.calculateTotalLikes();
            }
        });

        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Changes the online/offline status
     *
     * @return message.
     */
    @Override
    public String switchConnectionStatus() {
        connectionStatus = !connectionStatus;

        return this.getUsername() + " has changed status successfully.";
    }

    /**
     * Adds an album
     *
     * @param name album name
     * @param releaseYear release yer
     * @param description description
     * @param song list of songs
     * @param timestamp the timestamp of the addition
     * @return the error message since a normal user cant add an album
     */
    @Override
    public String addAlbum(final String name, final int releaseYear,
                           final String description, final List<SongInput> song,
                           final int timestamp) {
        return this.getUsername() + " is not an artist.";
    }

    /**
     * calls the print page method of its respective current page
     *
     * @return message
     */
    @Override
    public String printCurrentPage() {
        if (!connectionStatus) {
            return getUsername() + " is offline.";
        }

        Visitor visitor1 = new PrintPageVisitor();
        Visitor visitor2 = new UpdatePageVisitor();

        if (indexOfCurrentPage > 1) {
            User owner = Admin.getInstance().getUser(pageHistory.get(pageHistoryIndex).getOwner());

            if (owner == null) {
                owner = Admin.getInstance().getUser(player.getSource().getAudioCollection().getOwner());
            }

            pages[indexOfCurrentPage].accept(visitor2, owner);

            return pages[indexOfCurrentPage].accept(visitor1,
                    searchBar.getLastSelectedUser()).toString();
        } else {
            pages[indexOfCurrentPage].accept(visitor2, this);
            return pages[indexOfCurrentPage].accept(visitor1, this).toString();
        }
    }

    /**
     * changes to index of the current page
     *
     * @param page the name of the page we want to switch to
     * @return message
     */
    @Override
    public String changePage(final String page) {
        String message;

        switch (page) {
            case "Home" -> {
                indexOfCurrentPage = HOME_PAGE;
                message = getUsername() + " accessed Home successfully.";
            }
            case "LikedContent" -> {
                indexOfCurrentPage = LIKED_CONTENT_PAGE;
                message = getUsername() + " accessed LikedContent successfully.";
            }
            case "Artist" -> {
                indexOfCurrentPage = ARTIST_PAGE;
                message = getUsername() + " accessed Artist successfully.";
            }
            case "Host" -> {
                indexOfCurrentPage = HOST_PAGE;
                message = getUsername() + " accessed Host successfully.";
            }
            default -> {
                message = getUsername() + " is trying to access a non-existent page.";
            }
        }

        if (!message.contains("non")) {
            pageHistory.add(pages[indexOfCurrentPage]);
            pageHistoryIndex = pageHistory.size() - 1;
        }
        return message;
    }

    /**
     * Checks for the connections of the user
     *
     * @return a message
     */
    @Override
    public String checkIfUserCanBeDeleted() {
        for (Playlist playlist : playlists) {
            for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
                User user = Admin.getInstance().getUsers().get(i);
                Player player = user.getPlayer();

                if (player.getSource() != null) {
                    if (playlist.equals(player.getSource().getAudioCollection())) {
                        return getUsername() + " can't be deleted.";
                    }
                }
            }
        }

        return "ok";
    }

    /**
     * Deletes the connections of an user
     *
     * @return a message
     */
    @Override
    public String deleteUsersConnections() {
        for (Song song : likedSongs) {
            song.dislike();
        }

        for (Playlist playlist : followedPlaylists) {
            playlist.decreaseFollowers();
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            User user = Admin.getInstance().getUsers().get(i);

            user.getFollowedPlaylists().removeIf(playlist -> playlists.contains(playlist));
        }

        Admin.getInstance().setNoNormalUsers(Admin.getInstance().getNoNormalUsers() - 1);

        return getUsername() + " was successfully deleted.";
    }

    @Override
    public void calculateTotalLikes() { }

    /**
     * removes an album
     *
     * @param name album name
     * @return error message since a normal user can't delete an album
     */
    @Override
    public String removeAlbum(final String name) {
        return getUsername() + " is not an artist.";
    }

    /**
     * adds an event
     *
     * @param name event name
     * @param description event description
     * @param date event date
     * @return error message since a normal user can't add an event
     */
    @Override
    public String addEvent(final String name, final String description, final String date) {
        return getUsername() + " is not an artist.";
    }

    /**
     * removes an event
     *
     * @param name event name
     * @return error message since a normal user can't remove an event
     */
    @Override
    public String removeEvent(final String name) {
        return getUsername() + " is not an artist.";
    }

    /**
     * adds an announcement
     *
     * @param name announcement name
     * @param description announcement description
     * @return error message since a normal user can't add an announcement
     */
    @Override
    public String addAnnouncement(final String name, final String description) {
        return getUsername() + " is not a host.";
    }

    /**
     * removes an announcement
     *
     * @param name announcement name
     * @return error message since a normal user can't remove an announcement
     */
    @Override
    public String removeAnnouncement(final String name) {
        return getUsername() + " is not a host.";
    }

    /**
     * add a podcast
     *
     * @param name podcast name
     * @param episodes podcast episode
     * @return error message since a normal user can't add a podcast
     */
    @Override
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        return getUsername() + " is not a host.";
    }

    /**
     * removes a podcast
     *
     * @param name podcast name
     * @return error message since a normal user can't remove a podcast
     */
    @Override
    public String removePodcast(final String name) {
        return getUsername() + " is not a host.";
    }

    @Override
    public final ArrayList<Podcast> getPodcasts() {
        return null;
    }

    @Override
    public final ArrayList<Announcement> getAnnouncements() {
        return null;
    }

    @Override
    public final ArrayList<Album> getAlbums() {
        return null;
    }

    @Override
    public final ArrayList<Merch> getMerches() {
        return null;
    }

    @Override
    public final ArrayList<Event> getEvents() {
        return null;
    }

    /**
     * adds merch
     *
     * @param name merch name
     * @param descpription merch description
     * @param price merch price
     * @return error message since a normal user can't add merch
     */
    @Override
    public String addMerch(final String name, final String descpription, final int price) {
        return getUsername() + " is not an artist.";
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time, this);
    }

    /**
     * updates the song hashmap
     *
     * @param songName the name of the song
     */
    public void incrementSong(final String songName) {
        if (topSongs.containsKey(songName)) {
            topSongs.put(songName, topSongs.get(songName) + 1);
        } else {
            topSongs.put(songName, 1);
        }
    }

    /**
     * updates the artist hashmap
     *
     * @param artistName the name of the artist
     */
    public void incrementArtist(final String artistName) {
        if (topArtists.containsKey(artistName)) {
            topArtists.put(artistName, topArtists.get(artistName) + 1);
        } else {
            topArtists.put(artistName, 1);
        }
    }

    /**
     * updates the genre hashmap
     *
     * @param genreName the genre name
     */
    public void incrementGenre(final String genreName) {
        if (topGenres.containsKey(genreName)) {
            topGenres.put(genreName, topGenres.get(genreName) + 1);
        } else {
            topGenres.put(genreName, 1);
        }
    }

    /**
     * updates the albums hashmap
     *
     * @param albumName the name of the album
     */
    public void incrementAlbums(final String albumName) {
        if (topAlbums.containsKey(albumName)) {
            topAlbums.put(albumName, topAlbums.get(albumName) + 1);
        } else {
            topAlbums.put(albumName, 1);
        }
    }

    /**
     * updates the episodes hashmap
     *
     * @param episodeName the name of the episode
     */
    public void incrementEpisodes(final String episodeName) {
        if (topEpisodes.containsKey(episodeName)) {
            topEpisodes.put(episodeName, topEpisodes.get(episodeName) + 1);
        } else {
            topEpisodes.put(episodeName, 1);
        }
    }

    /**
     * gets all the wrapped info
     *
     * @return the json node with all the info
     */
    public ObjectNode getWrapped() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = new ObjectMapper().createObjectNode();

        if (topArtists.isEmpty() && topGenres.isEmpty() && topAlbums.isEmpty()
                && topSongs.isEmpty() && topEpisodes.isEmpty()) {
            return null;
        }

        result.put("topArtists", objectMapper.valueToTree(getTop5Entries(topArtists)));
        result.put("topGenres", objectMapper.valueToTree(getTop5Entries(topGenres)));
        result.put("topSongs", objectMapper.valueToTree(getTop5Entries(topSongs)));
        result.put("topAlbums", objectMapper.valueToTree(getTop5Entries(topAlbums)));
        result.put("topEpisodes", objectMapper.valueToTree(getTop5Entries(topEpisodes)));

        return result;
    }

    @Override
    public String buyMerch(final String merchName) {
        if (indexOfCurrentPage != ARTIST_PAGE) {
            return "Cannot buy merch from this page.";
        }

        Merch wantedMerch = null;

        ArtistUser artistUser = (ArtistUser) Admin.getInstance().getUser(pages[indexOfCurrentPage].getOwner());
        for (Merch merch : artistUser.getMerches()) {
            if (merch.getName().equals(merchName))
                wantedMerch = merch;
        }

        if (wantedMerch == null) {
            return "The merch " + merchName + " doesn't exist.";
        }

        boughtMerch.add(wantedMerch.getName());

        artistUser.setMerchRevenue(artistUser.getMerchRevenue() + wantedMerch.getPrice());
        artistUser.setSoldMerch(true);

        return getUsername() + " has added new merch successfully.";
    }

    @Override
    public String subscribe() {
        if (indexOfCurrentPage != ARTIST_PAGE && indexOfCurrentPage != HOST_PAGE) {
            return "To subscribe you need to be on the page of an artist or host.";
        }

        User user = Admin.getInstance().getUser(pages[indexOfCurrentPage].getOwner());

        ArrayList<User> subscribers = user.getSubscribers();

        if (subscribers.contains(this)) {
            subscribers.remove(this);
            return getUsername() + " unsubscribed from " + user.getUsername() + " successfully.";
        }

        subscribers.add(this);
        return getUsername() + " subscribed to " + user.getUsername() + " successfully.";
    }

    /**
     * update method to add a notification when needed
     *
     * @param name the name f the notification
     * @param description the description
     */
    @Override
    public void update(final String name, final String description) {
        notificationBar.add(new Notifications(name, description));
    }

    public ArrayList<User> getSubscribers() {
        return null;
    }

    /**
     * buys the premium subscription
     *
     * @return a message
     */
    @Override
    public String buyPremium() {
        if (premiumSubscription) {
            return getUsername() + " is already a premium user.";
        }

        premiumSubscription = true;
        return getUsername() + " bought the subscription successfully.";
    }

    /**
     * cancel the premium subscription of an user
     *
     * @return a message
     */
    @Override
    public String cancelPremium() {
        if (!premiumSubscription) {
            return getUsername() + " is not a premium user.";
        }

        premiumSubscription = false;

        int listenedSongs = listenedWhilePremium.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        for (Map.Entry<ArtistUser, Integer> entry
                : listenedWhilePremium.entrySet()) {
            ArtistUser artist = entry.getKey();
            int songs = entry.getValue();

            artist.setSongRevenue(Math.round((artist.getSongRevenue() + (float) 1000000 / listenedSongs * songs) / 100) * 100);
        }

        listenedWhilePremium.clear();
        return getUsername() + " cancelled the subscription successfully.";
    }

    /**
     * gets all the notifications and clear the notifications bar
     *
     * @return a list of notifications
     */
    public ArrayList<Notifications> getNotifications() {
        ArrayList<Notifications> copy = new ArrayList<>(notificationBar);

        notificationBar.clear();

        return copy;
    }

    /**
     * goes to the next page in the page history list
     *
     * @return a message
     */
    @Override
    public String nextPage() {
        if (pageHistoryIndex == pageHistory.size() - 1) {
            return "There are no pages left to go forward.";
        }

        pageHistoryIndex++;
        switch (pageHistory.get(pageHistoryIndex).getType()) {
            case "Home " -> {
                indexOfCurrentPage = HOME_PAGE;
            }
            case "LikedContent" -> {
                indexOfCurrentPage = LIKED_CONTENT_PAGE;
            }
            case "Artist" -> {
                indexOfCurrentPage = ARTIST_PAGE;
            }
            case "Host" -> {
                indexOfCurrentPage = HOST_PAGE;
            }
            default -> {
            }
        }
        return "The user " + getUsername() + " has navigated successfully to the next page.";
    }

    /**
     * goes to the previous page in the page history list
     *
     * @return a message
     */
    @Override
    public String prevPage() {
        if (pageHistoryIndex == 0) {
            return "There are no pages left to go back.";
        }

        pageHistoryIndex--;
        switch (pageHistory.get(pageHistoryIndex).getType()) {
            case "Home " -> {
                indexOfCurrentPage = HOME_PAGE;
            }
            case "LikedContent" -> {
                indexOfCurrentPage = LIKED_CONTENT_PAGE;
            }
            case "Artist" -> {
                indexOfCurrentPage = ARTIST_PAGE;
            }
            case "Host" -> {
                indexOfCurrentPage = HOST_PAGE;
            }
            default -> {
            }
        }

        return "The user " + getUsername() + " has navigated successfully to the previous page.";
    }

    /**
     * gets a list with all the merches bought by this user
     *
     * @return the list
     */
    public ArrayList<String> seeMyMerch() {
        return boughtMerch;
    }

    public Song getRecommendationSong() {
        String genre = null;
        int howMuchHasPlayed = player.getSource().getAudioFile().getDuration()
                - player.getSource().getRemainedDuration();

        if (howMuchHasPlayed >= 30) {
            genre = ((Song) player.getSource().getAudioFile()).getGenre();
        } else {
            return null;
        }

        List<Song> genreSong = new ArrayList<>();

        for (Song song : Admin.getInstance().getSongs()) {
            if (song.getGenre().equals(genre)) {
                genreSong.add(song);
            }
        }

        int index = new Random(howMuchHasPlayed).nextInt(genreSong.size());

        return genreSong.get(index);
    }

    /**
     * gets a map of the top 3 genres listened by the user
     *
     * @return the map
     */
    public Map<String, Integer> getTopGenres() {
        HashMap<String, Integer> listenedGenres = new HashMap<>();

        for (Song song : likedSongs) {
            if (listenedGenres.containsKey(song.getGenre())) {
                listenedGenres.put(song.getGenre(), listenedGenres.get(song.getGenre()) + 1);
            } else {
                listenedGenres.put(song.getGenre(), 1);
            }
        }

        for (Playlist playlist : playlists) {
            for (Song song : playlist.getSongs()) {
                if (listenedGenres.containsKey(song.getGenre())) {
                    listenedGenres.put(song.getGenre(), listenedGenres.get(song.getGenre()) + 1);
                } else {
                    listenedGenres.put(song.getGenre(), 1);
                }
            }
        }

        for (Playlist playlist : followedPlaylists) {
            for (Song song : playlist.getSongs()) {
                if (listenedGenres.containsKey(song.getGenre())) {
                    listenedGenres.put(song.getGenre(), listenedGenres.get(song.getGenre()) + 1);
                } else {
                    listenedGenres.put(song.getGenre(), 1);
                }
            }
        }

        return listenedGenres.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * return the playlist formed by the recommendations
     *
     * @return the playlisr
     */
    public Playlist getPlaylistRecommendation() {
        Set<String> topListenedGenres = getTopGenres().keySet();
        int maxInputs = 0;
        Playlist recommendedPlaylist = new Playlist(getUsername() + "'s recommendations", getUsername());

        Iterator<String> iterator = topListenedGenres.iterator();
        while (iterator.hasNext() && maxInputs < 3) {
            String genre = iterator.next();

            List<Song> genreSongs = new ArrayList<>();

            for (Song song : Admin.getInstance().getSongs()) {
                if (song.getGenre().equals(genre)) {
                    genreSongs.add(song);
                }
            }

            genreSongs.sort(new Comparator<Song>() {
                @Override
                public int compare(Song o1, Song o2) {
                    return o2.getLikes() - o1.getLikes();
                }
            });

            switch (maxInputs) {
                case 0 -> {
                    for (int i = 0; i < 5; i++) {
                        recommendedPlaylist.addSong(genreSongs.get(i));
                    }
                }
                case 1 -> {
                    for (int i = 0; i < 3; i++) {
                        recommendedPlaylist.addSong(genreSongs.get(i));
                    }
                }

                case 2 -> {
                    for (int i = 0; i < 2; i++) {
                        recommendedPlaylist.addSong(genreSongs.get(i));
                    }
                }
            }

            maxInputs++;
        }

        return recommendedPlaylist;
    }

    public Playlist getRecommendedFansPlaylist() {
        ArtistUser artist = (ArtistUser) Admin.getInstance().getUser(((Song) player.
                                            getSource().getAudioFile()).getArtist());

        Playlist fansPlaylist = new Playlist(artist.getUsername() + " Fan Club recommendations"
                , this.getUsername());

        Map<String, Integer> topFansSorted = artist.getTopFans().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        List<String> top5Fans = new ArrayList<>(topFansSorted.keySet())
                                    .subList(0, Math.min(5, topFansSorted.size()));

        for (String username : top5Fans) {
            NormalUser user = (NormalUser) Admin.getInstance().getUser(username);

            user.getLikedSongs().sort(new Comparator<Song>() {
                @Override
                public int compare(Song o1, Song o2) {
                    return o2.getLikes() - o1.getLikes();
                }
            });

            List<Song> top5UserSong = user.getLikedSongs()
                    .subList(0, Math.min(5, user.getLikedSongs().size()));

            for (Song song : top5UserSong) {
                fansPlaylist.addSong(song);
            }
        }

        return fansPlaylist;
    }

    /**
     * updates the recommendations for an user
     *
     * @param type type of the recommendation
     * @return a message
     */
    public String updateRecommendations(final String type) {
        switch (type) {
            case "random_playlist" -> {
                recommendedPlaylist = getPlaylistRecommendation();
                recommedation = RecommendationType.PLAYLIST;
            }
            case "random_song" -> {
                if (getRecommendationSong() == null) {
                    return "No new recommendations were found.";
                }
                recommendedSong = getRecommendationSong();
                recommedation = RecommendationType.SONG;
            }
            default -> {
                recommendedPlaylist = getRecommendedFansPlaylist();
                recommedation = RecommendationType.PLAYLIST;
            }
        }

        return "The recommendations for user " + getUsername() + " have been updated successfully.";
    }

    public String loadRecommendations() {
        if (recommedation.equals(RecommendationType.NONE)) {
            return "No recommendations available.";
        }

        if (!isConnectionStatus()) {
            return getUsername() + " is offline.";
        }

        if (recommedation.equals(RecommendationType.PLAYLIST)) {
            player.setSource(recommendedPlaylist, "playlist");
            player.pause();

            Admin.getInstance().updateUserWrapped(this);
        } else {
            player.setSource(recommendedSong, "song");
            player.pause();

            Admin.getInstance().updateUserWrapped(this);
        }

        return "Playback loaded successfully.";
    }
    public final boolean isConnectionStatus() {
        return connectionStatus;
    }

    public final void setConnectionStatus(final boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public final ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public final void setPlaylists(final ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public final ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }

    public final void setLikedSongs(final ArrayList<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public final ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public final void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    @Override
    public final Player getPlayer() {
        return player;
    }

    public final SearchBar getSearchBar() {
        return searchBar;
    }

    public final boolean isLastSearched() {
        return lastSearched;
    }

    public final void setLastSearched(final boolean lastSearched) {
        this.lastSearched = lastSearched;
    }

    public void setRecommendedSong(Song recommendedSong) {
        this.recommendedSong = recommendedSong;
    }

    @Override
    public Song getRecommendedSong() {
        return recommendedSong;
    }

    public Playlist getRecommendedPlaylist() {
        return recommendedPlaylist;
    }

    public void setRecommendedPlaylist(Playlist recommendedPlaylist) {
        this.recommendedPlaylist = recommendedPlaylist;
    }

    public RecommendationType getRecommendationType() {
        return recommedation;
    }

    public void setRecommendationType(RecommendationType recommedaion) {
        this.recommedation = recommedaion;
    }

    public HashMap<ArtistUser, Integer> getListenedWhilePremium() {
        return listenedWhilePremium;
    }

    public void setListenedWhilePremium(HashMap<ArtistUser, Integer> listenedWhilePremium) {
        this.listenedWhilePremium = listenedWhilePremium;
    }
}
