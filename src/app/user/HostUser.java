package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.*;

public class HostUser extends User {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    private HashMap<String, Integer> topEpisodes;
    private HashSet<String> listeners;
    private boolean isListened;


    public HostUser(final String username, final int age, final String city) {
        super(username, age, city);
        announcements = new ArrayList<>();
        podcasts = new ArrayList<>();
        topEpisodes = new HashMap<>();
        listeners = new HashSet<>();
        isListened = false;
    }

    /**
     * adds a podcast
     *
     * @param name podcast name
     * @param episodes list of episodes
     * @return a message
     */
    @Override
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return getUsername() + " has another podcast with the same name.";
            }
        }

        Set<String> uniqueueEpisodes = new HashSet<>();
        for (EpisodeInput episodeInput : episodes) {
            if (!uniqueueEpisodes.add(episodeInput.getName())) {
                return getUsername() + " has the same episode in this podcast.";
            }
        }
        List<Episode> episodeList = new ArrayList<>();
        for (EpisodeInput episodeInput : episodes) {
            episodeList.add(new Episode(episodeInput.getName(),
                    episodeInput.getDuration(),
                    episodeInput.getDescription()));
        }

        podcasts.add(new Podcast(name, getUsername(), episodeList));
        Admin.getInstance().getPodcasts().add(new Podcast(name, getUsername(), episodeList));
        return getUsername() + " has added new podcast successfully.";
    }


    /**
     * removes a podcast
     *
     * @param name the podcast name
     * @return a message
     */
    @Override
    public String removePodcast(final String name) {
        Podcast podcast = null;
        for (Podcast podcast1 : podcasts) {
            if (podcast1.getName().equals(name)) {
                podcast = podcast1;
                break;
            }
        }

        if (podcast == null) {
            return getUsername() + " doesn't have a podcast with the given name.";
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            Player player = ((NormalUser) Admin.getInstance().
                    getUsers().get(i)).getPlayer();

            if (player.getSource() != null) {
                if (player.getSource().getAudioCollection().equals(podcast)) {
                    return getUsername() + " can't delete this podcast.";
                }
            }
        }

        podcasts.remove(podcast);
        Admin.getInstance().getPodcasts().remove(podcast);

        return getUsername() + " deleted the podcast successfully.";
    }

    /**
     * adds an announcement
     *
     * @param name announcement name
     * @param description announcement description
     * @return a message
     */
    @Override
    public String addAnnouncement(final String name, final String description) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return getUsername() + " has already added an announcement with the same name.";
            }
        }

        announcements.add(new Announcement(name, description));
        return getUsername() + " has successfully added new announcement.";
    }

    /**
     * removes an announcement
     *
     * @param name announcement name
     * @return a message
     */
    @Override
    public String removeAnnouncement(final String name) {
        Announcement announcement = null;
        for (Announcement announcement1 : announcements) {
            if (announcement1.getName().equals(name)) {
                announcement = announcement1;
                break;
            }
        }

        if (announcement == null) {
            return getUsername() + " has no announcement with the given name.";
        }

        announcements.remove(announcement);
        return getUsername() + " has successfully deleted the announcement.";
    }

    /**
     * checks if the host can be deleted
     *
     * @return a message
     */
    @Override
    public String checkIfUserCanBeDeleted() {
        for (Podcast podcast : podcasts) {
            for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
                User user = Admin.getInstance().getUsers().get(i);
                Player player = user.getPlayer();

                if (player.getSource() != null) {
                    if (podcast.equals(player.getSource().getAudioCollection())) {
                        return getUsername() + " can't be deleted.";
                    }
                }
            }
        }

        for (int i = 0; i < Admin.getInstance().getNoNormalUsers(); i++) {
            User user = Admin.getInstance().getUsers().get(i);
            int indexOfPage = user.getIndexOfCurrentPage();

            if (indexOfPage == 2 + 1) {
                if (user.getPages()[2 + 1].getOwner().equals(getUsername())) {
                    return getUsername() + " can't be deleted.";
                }
            }
        }

        return "ok";
    }

    /**
     * deletes the host and all of its connections
     *
     * @return a message
     */
    @Override
    public String deleteUsersConnections() {
        Iterator<Podcast> iterator = podcasts.iterator();

        while (iterator.hasNext()) {
            Admin.getInstance().getPodcasts().remove(iterator.next());
            iterator.remove();
        }

        Admin.getInstance().setNoHostUsers(Admin.getInstance().getNoHostUsers() - 1);

        return getUsername() + " was successfully deleted.";
    }

    /**
     * increments the number of listens that this episode has
     *
     * @param episodeName the name of the episode
     */
    public void incrementTopEpisodes(final String episodeName) {
        if (topEpisodes.containsKey(episodeName)) {
            topEpisodes.put(episodeName, topEpisodes.get(episodeName) + 1);
        } else {
            topEpisodes.put(episodeName, 1);
        }
    }

    /**
     * adds the username to the listeners set, so they don't appear twice
     *
     * @param userName the username
     */
    public void incrementListeners(final String userName) {
        listeners.add(userName);
    }

    /**
     * gets all the wrapped info
     *
     * @return the node with the info
     */
    public ObjectNode getWrapped() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        if (!isListened()) {
            return null;
        }

        result.put("topEpisodes", objectMapper.valueToTree(getTop5Entries(topEpisodes)));
        result.put("listeners", listeners.size());

        return result;
    }

    @Override
    public String buyMerch(String merchName) {
        return getUsername() + " is not a normal user.";
    }

    @Override
    public ArrayList<String> seeMyMerch() {
        return null;
    }

    @Override
    public void calculateTotalLikes() { }

    /**
     * returns null since a host doesn't have playlists
     *
     * @return null
     */
    @Override
    public List<Playlist> getPlaylists() {
        return null;
    }

    /**
     * @return error message since a host can't be online or offline
     */
    @Override
    public String switchConnectionStatus() {
        return this.getUsername() + " is not a normal user.";
    }

    /**
     * @return error message since a host can't add an album
     */
    @Override
    public String addAlbum(final String name, final int releaseYear,
                           final String description, final List<SongInput> songs,
                           final int timestamp) {
        return this.getUsername() + " is not an artist.";
    }

    /**
     * @return error message since a host can't add merch
     */
    @Override
    public String addMerch(final String name, final String description, final int price) {
        return getUsername() + " is not an artist.";
    }

    /**
     * @return error message since a host can't remove an album
     */
    @Override
    public String removeAlbum(final String name) {
        return getUsername() + " is not an artist.";
    }

    /**
     * check to see if the host has the username given
     *
     * @param name the username
     * @return true or false
     */
    @Override
    public boolean matchesName(final String name) {
        return getUsername().startsWith(name);
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

    @Override
    public final List<Song> getLikedSongs() {
        return null;
    }

    @Override
    public final List<Playlist> getFollowedPlaylists() {
        return null;
    }

    /**
     * @return error message since a host can't print the page
     */
    @Override
    public String printCurrentPage() {
        return getUsername() + " is not a normal user.";
    }

    /**
     * changes the page
     *
     * @param page the page we want to go to
     * @return error message since a host can't change the page
     */
    @Override
    public String changePage(final String page) {
        return getUsername() + " is not a normal user.";
    }

    /**
     * @return error message since a host can't add events
     */
    @Override
    public String addEvent(final String name, final String description, final String date) {
        return getUsername() + " is not an artist.";
    }

    /**
     * @return error message since a host can't remove events
     */
    @Override
    public String removeEvent(final String name) {
        return getUsername() + " is not an artist.";
    }

    @Override
    public final ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public final void setPodcasts(final ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public final ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public final void setAnnouncements(final ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    public boolean isListened() {
        return isListened;
    }

    public void setListened(boolean listened) {
        isListened = listened;
    }
}
