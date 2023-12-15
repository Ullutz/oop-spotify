package app.audio.Collections;

import app.utils.Enums;

import java.util.ArrayList;

public class PlaylistOutput {
    private final String name;
    private final ArrayList<String> songs;
    private final String visibility;
    private final int followers;


    public PlaylistOutput(final Playlist playlist) {
        this.name = playlist.getName();
        this.songs = new ArrayList<>();
        for (int i = 0; i < playlist.getSongs().size(); i++) {
            songs.add(playlist.getSongs().get(i).getName());
        }
        this.visibility = playlist.getVisibility() == Enums.Visibility.PRIVATE
                                                      ? "private" : "public";
        this.followers = playlist.getFollowers();
    }

    public final String getName() {
        return name;
    }

    public final ArrayList<String> getSongs() {
        return songs;
    }

    public final String getVisibility() {
        return visibility;
    }

    public final int getFollowers() {
        return followers;
    }
}
