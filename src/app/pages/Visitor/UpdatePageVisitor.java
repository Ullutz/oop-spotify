package app.pages.Visitor;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.NormalUser;
import app.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UpdatePageVisitor implements Visitor {

    /**
     * updates the contents of a likedcontentpage
     *
     * @param page the page
     * @param user the user
     * @return a stringbuilder
     */
    @Override
    public StringBuilder visit(final LikedContentPage page, final User user) {
        page.getLikedSongs().clear();
        page.getLikedSongs().addAll(((NormalUser) user).getLikedSongs());

        page.getFollowedPlaylists().clear();
        page.getFollowedPlaylists().addAll(((NormalUser) user).getFollowedPlaylists());

        return null;
    }

    /**
     * updates the contents of an artistpage
     *
     * @param page the page
     * @param user the user
     * @return a stringbuilder
     */
    @Override
    public StringBuilder visit(final ArtistPage page, final User user) {
        page.setOwner(user.getUsername());
        page.getAlbums().clear();
        page.getMerches().clear();
        page.getEvents().clear();

        page.getAlbums().addAll(user.getAlbums());
        page.getMerches().addAll(user.getMerches());
        page.getEvents().addAll(user.getEvents());
        return null;
    }

    /**
     * updates the contents of a hostpage
     *
     * @param page the page
     * @param user the user
     * @return a stringbuilder
     */
    @Override
    public StringBuilder visit(final HostPage page, final User user) {
        page.setOwner(user.getUsername());
        page.getPodcasts().clear();
        page.getAnnouncements().clear();

        page.getPodcasts().addAll(user.getPodcasts());
        page.getAnnouncements().addAll(user.getAnnouncements());
        return null;
    }

    /**
     * updates the contents of a homepage
     *
     * @param page the page
     * @param user the user
     * @return a stringbuilder
     */
    @Override
    public StringBuilder visit(final HomePage page, final User user) {
        page.getFirst5LikedSongs().clear();
        List<Song> sortedList = new ArrayList<>(user.getLikedSongs());
        sortedList.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song o1, final Song o2) {
                return o2.getLikes() - o1.getLikes();
            }
        });

        for (int i = 0; i < MAX && i < user.getLikedSongs().size(); i++) {
            page.getFirst5LikedSongs().add(sortedList.get(i));
        }

        page.getTop5LikedPlaylists().clear();

        List<Playlist> sortedPlaylists = new ArrayList<>(user.getFollowedPlaylists());

        sortedPlaylists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist o1, final Playlist o2) {
                int a = o1.calculateTotalLikes();
                int b = o2.calculateTotalLikes();

                return b - a;
            }
        });

        for (int i = 0; i < MAX && i < user.getFollowedPlaylists().size(); i++) {
            page.getTop5LikedPlaylists().add(sortedPlaylists.get(i));
        }

        if (!page.getSongRecommendations().contains(user.getRecommendedSong())
            && user.getRecommendedSong() != null) {
            page.getSongRecommendations().add(user.getRecommendedSong());
        }

        if (!page.getPlaylistRecommendations().contains(user.getRecommendedPlaylist())
            && user.getRecommendedPlaylist() != null) {
            page.getPlaylistRecommendations().add(user.getRecommendedPlaylist());
        }

        return null;
    }
}
