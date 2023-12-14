package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Album extends Playlist {
    private int totalLikes;
    private String description;
    private int releaseYear;

    /**
     * Instantiates a new Audio collection.
     *
     * @param name      the name
     * @param owner     the owner
     * @param timestamp
     */
    public Album(final String name, final String owner,
                 final String description, final int releaseYear, int timestamp) {
        super(name, owner, timestamp);
        this.description = description;
        this.releaseYear = releaseYear;
    }

    @Override
    public Integer calculateTotalLikes() {
        totalLikes = 0;
        for (Song song : getSongs())
            totalLikes += song.getLikes();

        return totalLikes;
    }

    @Override
    public boolean matchesDescription(String description) {
        return this.description.startsWith(description);
    }

    @Override
    public int getNumberOfTracks() {
        return getSongs().size();
    }

    @Override
    public ArrayList<Song> getSongs() {
        return super.getSongs();
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
