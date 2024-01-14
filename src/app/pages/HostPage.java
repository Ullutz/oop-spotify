package app.pages;

import app.pages.Visitor.Visitable;
import app.pages.Visitor.Visitor;
import app.audio.Collections.Podcast;
import app.user.Announcement;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

import static app.utils.Enums.HOST_PAGE;

public class HostPage implements Page, Visitable {
    private final int TYPE = HOST_PAGE;
    private String owner;
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();

    @Override
    public final StringBuilder accept(final Visitor visitor, final User user) {
        return visitor.visit(this, user);
    }

    @Override
    public final String getOwner() {
        return owner;
    }

    @Override
    public final void setOwner(final String username) {
        owner = username;
    }

    public final List<Podcast> getPodcasts() {
        return podcasts;
    }

    public final void setPodcasts(final List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public final List<Announcement> getAnnouncements() {
        return announcements;
    }

    public final void setAnnouncements(final List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public final String getType() {
        return "Host";
    }
}
