package app.player;

import app.utils.Enums;

public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private String repeat;
    private final boolean shuffle;
    private final boolean paused;

    public PlayerStats(final String name,
                       final int remainedTime,
                       final Enums.RepeatMode repeatMode,
                       final boolean shuffle,
                       final boolean paused) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.paused = paused;
        switch (repeatMode) {
            case REPEAT_ALL -> {
                this.repeat = "Repeat All";
            }
            case REPEAT_ONCE -> {
                this.repeat = "Repeat Once";
            }
            case REPEAT_INFINITE -> {
                this.repeat = "Repeat Infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                this.repeat = "Repeat Current Song";
            }
            case NO_REPEAT -> {
                this.repeat = "No Repeat";
            }
            default -> {
                this.repeat = "";
            }
        }
        this.shuffle = shuffle;
    }

    public final String getName() {
        return name;
    }

    public final int getRemainedTime() {
        return remainedTime;
    }

    public final String getRepeat() {
        return repeat;
    }

    public final void setRepeat(final String repeat) {
        this.repeat = repeat;
    }

    public final boolean isShuffle() {
        return shuffle;
    }

    public final boolean isPaused() {
        return paused;
    }
}
