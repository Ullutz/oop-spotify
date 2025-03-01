package app.player;

public final class PodcastBookmark {
    private final String name;
    private final int id;
    private final int timestamp;

    public PodcastBookmark(final String name,
                           final int id,
                           final int timestamp) {
        this.name = name;
        this.id = id;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PodcastBookmark{"
                + "name='" + name + '\''
                + ", id=" + id
                + ", timestamp=" + timestamp
                + '}';
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
