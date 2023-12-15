package app.audio.Files;

import app.audio.LibraryEntry;

public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;

    public AudioFile(final String name, final Integer duration) {
        super(name);
        this.duration = duration;
    }

    public final Integer getDuration() {
        return duration;
    }
}
