package app.pages.Visitor;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Announcement;
import app.user.Event;
import app.user.Merch;
import app.user.User;

public class PrintPageVisitor implements Visitor {
    /**
     * prints the contents of a likedcontentpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    @Override
    public StringBuilder visit(final LikedContentPage page, final User user) {
        StringBuilder message = new StringBuilder("Liked songs:\n\t[");
        for (Song song : page.getLikedSongs()) {
            message.append(song.getName()).append(" - ").
                    append(song.getArtist()).append(", ");
        }

        if (!page.getLikedSongs().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nFollowed playlists:\n\t[");
        for (Playlist playlist : page.getFollowedPlaylists()) {
            message.append(playlist.getName()).append(" - ").
                    append(playlist.getOwner()).append(", ");
        }

        if (!page.getFollowedPlaylists().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]");

        return message;
    }

    /**
     * prints the contents of an artistpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    @Override
    public StringBuilder visit(final ArtistPage page, final User user) {
        StringBuilder message = new StringBuilder("Albums:\n\t[");
        for (Album album : page.getAlbums()) {
            message.append(album.getName()).append(", ");
        }

        if (!page.getAlbums().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nMerch:\n\t[");

        for (Merch merch : page.getMerches()) {
            message.append(merch.getName()).append(" - ").append(merch.getPrice());
            message.append(":\n\t").append(merch.getDescription()).append(", ");
        }

        if (!page.getMerches().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nEvents:\n\t[");

        for (Event event : page.getEvents()) {
            message.append(event.getName()).append(" - ").append(event.getDate());
            message.append(":\n\t").append(event.getDescription()).append(", ");
        }

        if (!page.getEvents().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]");
        return message;
    }


    /**
     * prints the contents of a hostpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    @Override
    public StringBuilder visit(final HostPage page, final User user) {
        StringBuilder message = new StringBuilder("Podcasts:\n\t[");
        for (Podcast podcast : page.getPodcasts()) {
            message.append(podcast.getName()).append(":\n\t[");
            for (Episode episode : podcast.getEpisodes()) {
                message.append(episode.getName()).append(" - ").
                        append(episode.getDescription()).append(", ");
            }

            message.delete(message.length() - 2, message.length());
            message.append("]\n, ");
        }

        if (!page.getPodcasts().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nAnnouncements:\n\t[");
        for (Announcement announcement : page.getAnnouncements()) {
            message.append(announcement.getName()).append(":\n\t").
                    append(announcement.getDescription()).append("\n,");
        }

        if (!page.getAnnouncements().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]");
        return message;
    }

    /**
     * prints the contents of a homepage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    @Override
    public StringBuilder visit(final HomePage page, final User user) {
        StringBuilder message;

        message = new StringBuilder("Liked songs:\n\t[");
        for (Song song : page.getFirst5LikedSongs()) {
            message.append(song.getName()).append(", ");
        }

        if (!page.getFirst5LikedSongs().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nFollowed playlists:\n\t[");
        for (Playlist playlist : page.getTop5LikedPlaylists()) {
            message.append(playlist.getName()).append(", ");
        }

        if (!page.getTop5LikedPlaylists().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nSong recommendations:\n\t[");
        for (Song song : page.getSongRecommendations()) {
            message.append(song.getName()).append(", ");
        }

        if (!page.getSongRecommendations().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]\n\nPlaylists recommendations:\n\t[");
        for (Playlist playlist : page.getPlaylistRecommendations()) {
            message.append(playlist.getName()).append(", ");
        }

        if (!page.getPlaylistRecommendations().isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("]");

        return message;
    }
}
