package app.Pages;

import app.audio.Collections.Album;
import app.user.Event;
import app.user.Merch;
import app.user.NormalUser;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class ArtistPage implements Page {
    private String owner;
    List<Album> albums = new ArrayList<>();
    List<Merch> merches = new ArrayList<>();
    List<Event> events = new ArrayList<>();

    @Override
    public StringBuilder printCurrentPage(User user) {
        this.updatePage(user);

        StringBuilder message = new StringBuilder("Albums:\n\t[");
        for (Album album : albums) {
            message.append(album.getName()).append(", ");
        }

        if (!albums.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nMerch:\n\t[");

        for (Merch merch : merches) {
            message.append(merch.getName()).append(" - ").append(merch.getPrice());
            message.append(":\n\t").append(merch.getDescription()).append(", ");
        }

        if (!merches.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]\n\nEvents:\n\t[");

        for (Event event : events) {
            message.append(event.getName()).append(" - ").append(event.getDate());
            message.append(":\n\t").append(event.getDescription()).append(", ");
        }

        if (!events.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        message.append("]");
        return message;
    }

    @Override
    public void updatePage(User user) {
        owner = user.getUsername();
        albums.clear();
        merches.clear();
        events.clear();

        albums.addAll(user.getAlbums());
        merches.addAll(user.getMerches());
        events.addAll(user.getEvents());
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
