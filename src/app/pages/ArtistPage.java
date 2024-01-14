package app.pages;

import app.pages.Visitor.Visitor;
import app.audio.Collections.Album;
import app.user.Event;
import app.user.Merch;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

import static app.utils.Enums.ARTIST_PAGE;

public class ArtistPage implements Page {
    private final int TYPE = ARTIST_PAGE;
    private String owner;
    private List<Album> albums = new ArrayList<>();
    private List<Merch> merches = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    @Override
    public final StringBuilder accept(final Visitor visitor, final User user) {
        return visitor.visit(this, user);
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }

    public final List<Album> getAlbums() {
        return albums;
    }

    public final void setAlbums(final List<Album> albums) {
        this.albums = albums;
    }

    public final List<Merch> getMerches() {
        return merches;
    }

    public final void setMerches(final List<Merch> merches) {
        this.merches = merches;
    }

    public final List<Event> getEvents() {
        return events;
    }

    public final void setEvents(final List<Event> events) {
        this.events = events;
    }

    public final String getType() {
        return "Artist";
    }
}
