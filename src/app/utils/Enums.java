package app.utils;

public final class Enums { // diferite enumuri, le-am gurpat pe toate intr-un loc
   public static final int MAXDAY = 31;
   public static final int MAXMOUTH = 12;
   public static final int MAXDAYFORFEB = 28;
   public static final int MAXYEAR = 2023;
   public static final int MINYEAR = 1900;
   public static final int THREE = 3;
   public static final int FIVE = 5;
   public static final int SIX = 6;
   public static final int TEN = 10;
   public static final int TOP_ARTISTS = 0;
   public static final int TOP_GENRES = 1;
   public static final int TOP_SONGS = 2;
   public static final int TOP_ALBUMS = 3;
   public static final int TOP_EPISODES = 4;
   public static final int HOME_PAGE = 0;
   public static final int LIKED_CONTENT_PAGE = 1;
   public static final int ARTIST_PAGE = 2;
   public static final int HOST_PAGE = 3;

   private Enums() { }
    public enum Genre {
        POP,
        ROCK,
        RAP
    } // etc

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    public enum SearchType {
        SONG,
        PLAYLIST,
        PODCAST
    }

    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONCE, REPEAT_INFINITE, REPEAT_CURRENT_SONG, NO_REPEAT,
    }

    public enum PlayerSourceType {
        LIBRARY, PLAYLIST, PODCAST, ALBUM
    }

    public enum RecommendationType {
       NONE, SONG, PLAYLIST
    }
}
