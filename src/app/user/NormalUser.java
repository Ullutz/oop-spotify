package app.user;

import app.Admin;
import app.Pages.*;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Builder;

import java.util.*;

public class NormalUser extends User {
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private boolean connectionStatus;

    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        connectionStatus = true;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        pages[0] = new HomePage();
        pages[1] = new LikedContentPage();
        pages[2] = new ArtistPage();
        pages[3] = new HostPage();
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

    public String selectHelper(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist") ||
            searchBar.getLastSearchType().equals("host")) {
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

    public String selectArtistOrHost(final int itemNumber) {

        User selectedUser = searchBar.selectUser(itemNumber);

        if (selectedUser == null) {
            return "The selected ID is too high.";
        }

        if (searchBar.getLastSearchType().equals("artist")) {
            pages[2].setOwner(selectedUser.getUsername());
            changePage("ArtistPage");
        } else {
            pages[3].setOwner(selectedUser.getUsername());
            changePage("HostPage");
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

        if (!player.getType().equals("playlist")) {
            return "The loaded source is not a playlist.";
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

        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        if (!connectionStatus) {
            return getUsername() + " is offline.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
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

        followedPlaylists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
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

    @Override
    public String addAlbum(String name, int releaseYear,
                           String desciption, List<SongInput> song, int timestamp) {
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

        if (indexOfCurrentPage > 1) {
            return pages[indexOfCurrentPage].
                    printCurrentPage(searchBar.getLastSelectedUser()).
                    toString();
        } else {
            return pages[indexOfCurrentPage].printCurrentPage(this).toString();
        }
    }

    /**
     * changes to index of the current page
     *
     * @param page the name of the page we want to switch to
     * @return message
     */
    @Override
    public String changePage(String page) {
        String message = null;

        switch (page) {
            case "Home" -> {
                indexOfCurrentPage = 0;
                message = getUsername() + " accessed Home successfully.";
            }
            case "LikedContent" -> {
                indexOfCurrentPage = 1;
                message = getUsername() + " accessed LikedContent successfully.";
            }
            case "ArtistPage" -> {
                indexOfCurrentPage = 2;
            }
            case "HostPage" -> {
                indexOfCurrentPage = 3;
            }
            default -> message = getUsername() + " is trying to access a non-existent page.";
        }

        return message;
    }

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
    public String removeAlbum(String name) {
        return getUsername() + " is not an artist.";
    }

    @Override
    public String addEvent(final String name, final String description, final String date) {
        return getUsername() + " is not an artist.";
    }

    @Override
    public String removeEvent(String name) {
        return getUsername() + " is not an artist.";
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
    public ArrayList<Album> getAlbums() {
        return null;
    }

    @Override
    public ArrayList<Merch> getMerches() {
        return null;
    }

    @Override
    public ArrayList<Event> getEvents() {
        return null;
    }

    @Override
    public String addMerch(String name, String descpription, int price) {
        return getUsername() + " is not an artist.";
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }

    public boolean isConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(ArrayList<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public void setFollowedPlaylists(ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public SearchBar getSearchBar() {
        return searchBar;
    }

    public boolean isLastSearched() {
        return lastSearched;
    }

    public void setLastSearched(boolean lastSearched) {
        this.lastSearched = lastSearched;
    }
}
