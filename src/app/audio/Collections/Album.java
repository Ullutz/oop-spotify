package app.audio.Collections;

import app.audio.Files.Song;

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
                 final String description, final int releaseYear,
                 final int timestamp) {
        super(name, owner, timestamp);
        this.description = description;
        this.releaseYear = releaseYear;
    }

    /**
     * calculates the total likes of the album
     *
     * @return the total likes
     */
    @Override
    public Integer calculateTotalLikes() {
        totalLikes = 0;
        for (Song song : getSongs()) {
            totalLikes += song.getLikes();
        }

        return totalLikes;
    }

    /**
     * checks to see if the description of the album starts with the given one
     *
     * @param description the description
     * @return true or false
     */
    @Override
    public boolean matchesDescription(final String description) {
        return this.description.startsWith(description);
    }

    /**
     * @return the number of tracks
     */
    @Override
    public int getNumberOfTracks() {
        return getSongs().size();
    }

    @Override
    public final ArrayList<Song> getSongs() {
        return super.getSongs();
    }

    public final int getTotalLikes() {
        return totalLikes;
    }

    public final void setTotalLikes(final int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
