package app.Pages;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Announcement;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class HostPage implements Page {
    private String owner;
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();

    @Override
    public final StringBuilder printCurrentPage(final User user) {
        updatePage(user);

        StringBuilder message = new StringBuilder("Podcasts:\n\t[");
        for (Podcast podcast : podcasts) {
            message.append(podcast.getName()).append(":\n\t[");
            for (Episode episode : podcast.getEpisodes()) {
                message.append(episode.getName()).append(" - ").
                        append(episode.getDescription()).append(", ");
            }

            message.delete(message.length() - 2, message.length());
            message.append("]\n, ");
        }

        if (!podcasts.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nAnnouncements:\n\t[");
        for (Announcement announcement : announcements) {
            message.append(announcement.getName()).append(":\n\t").
                    append(announcement.getDescription()).append("\n,");
        }

        if (!announcements.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }

        message.append("\n]");
        return message;
    }

    @Override
    public final void updatePage(final User user) {
        podcasts.clear();
        announcements.clear();

        podcasts.addAll(user.getPodcasts());
        announcements.addAll(user.getAnnouncements());
    }

    @Override
    public final String getOwner() {
        return owner;
    }

    @Override
    public final void setOwner(final String username) {
        owner = username;
    }
}
