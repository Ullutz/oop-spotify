package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.*;

public class HostUser extends User {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;


    public HostUser(final String username, final int age, final String city) {
        super(username, age, city);
        announcements = new ArrayList<>();
        podcasts = new ArrayList<>();
    }

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
                if (player.getSource().getAudioCollection().equals(podcast))
                    return getUsername() + " can't delete this podcast.";
            }
        }

        podcasts.remove(podcast);
        Admin.getInstance().getPodcasts().remove(podcast);

        return getUsername() + " deleted the podcast successfully.";
    }

    @Override
    public String addAnnouncement(String name, String description) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return getUsername() + " has already added an announcement with the same name.";
            }
        }

        announcements.add(new Announcement(name, description));
        return getUsername() + " has successfully added new announcement.";
    }

    @Override
    public String removeAnnouncement(String name) {
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

            if (indexOfPage == 3) {
                if (user.getPages()[3].getOwner().equals(getUsername())) {
                    return getUsername() + " can't be deleted.";
                }
            }
        }

        return "ok";
    }

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

    @Override
    public void calculateTotalLikes() {}

    @Override
    public List<Playlist> getPlaylists() {
        return null;
    }

    @Override
    public String switchConnectionStatus() {
        return this.getUsername() + " is not a normal user.";
    }

    @Override
    public String addAlbum(String name, int releaseYear,
                           String desciption, List<SongInput> songs, final int timetamp) {
        return this.getUsername() + " is not an artist.";
    }

    @Override
    public String addMerch(String name, String descpription, int price) {
        return getUsername() + " is not an artist.";
    }

    @Override
    public String removeAlbum(String name) {
        return getUsername() + " is not an artist.";
    }

    @Override
    public boolean matchesName(String name) {
        return getUsername().startsWith(name);
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
    public List<Song> getLikedSongs() {
        return null;
    }

    @Override
    public List<Playlist> getFollowedPlaylists() {
        return null;
    }

    @Override
    public String printCurrentPage() {
        return getUsername() + " is not a normal user.";
    }

    @Override
    public String changePage(String page) {
        return getUsername() + " is not a normal user.";
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
    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }
}
