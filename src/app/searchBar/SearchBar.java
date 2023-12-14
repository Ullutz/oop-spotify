package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> resultsAudio;
    private List<User> resultsUsers;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private String lastSearchType;
    private LibraryEntry lastSelectedAudio;
    private User lastSelectedUser;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.resultsAudio = new ArrayList<>();
        this.resultsUsers = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelectedAudio = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> searchAudio(final Filters filters, final String type) {
        resultsUsers.clear();
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getInstance().getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getInstance().getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getInstance().getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getInstance().getAlbums());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }
                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }

                break;
            case "host":
                entries = new ArrayList<>();
                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.resultsAudio = entries;
        this.lastSearchType = type;
        return this.resultsAudio;
    }

    public List<User> searchArtistsOrHosts(final Filters filters, final String type) {
        resultsAudio.clear();
        List<User> entriesUsers;

        if (type.equals("artist"))
            entriesUsers = new ArrayList<>(Admin.getInstance().getArtists());
        else
            entriesUsers = new ArrayList<>(Admin.getInstance().getHosts());

        entriesUsers = filterUserByName(entriesUsers, filters.getName());

        while (entriesUsers.size() > MAX_RESULTS) {
            entriesUsers.remove(entriesUsers.size() - 1);
        }

        this.resultsUsers = entriesUsers;
        this.lastSearchType = type;
        return this.resultsUsers;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry selectAudio(final Integer itemNumber) {
        if (this.resultsAudio.size() < itemNumber) {
            resultsAudio.clear();
            resultsUsers.clear();

            return null;
        } else {
            lastSelectedAudio =  this.resultsAudio.get(itemNumber - 1);
            resultsAudio.clear();
            resultsUsers.clear();

            return lastSelectedAudio;
        }
    }

    public User selectUser(final Integer itemNumber) {
        if (this.resultsUsers.size() < itemNumber) {
            resultsAudio.clear();
            resultsUsers.clear();

            return null;
        } else {
            lastSelectedUser = this.resultsUsers.get(itemNumber - 1);
            resultsAudio.clear();
            resultsUsers.clear();

            return lastSelectedUser;
        }
    }

    public List<LibraryEntry> getAudioResults() {
        return resultsAudio;
    }

    public void setAudioResults(List<LibraryEntry> results) {
        this.resultsAudio = results;
    }

    public String getUser() {
        return user;
    }

    public String getLastSearchType() {
        return lastSearchType;
    }

    public void setLastSearchType(String lastSearchType) {
        this.lastSearchType = lastSearchType;
    }

    public LibraryEntry getLastSelectedAudio() {
        return lastSelectedAudio;
    }

    public void setLastSelectedAudio(LibraryEntry lastSelected) {
        this.lastSelectedAudio = lastSelected;
    }

    public List<LibraryEntry> getResultsAudio() {
        return resultsAudio;
    }

    public void setResultsAudio(List<LibraryEntry> resultsAudio) {
        this.resultsAudio = resultsAudio;
    }

    public List<User> getResultsUsers() {
        return resultsUsers;
    }

    public void setResultsUsers(List<User> resultsUsers) {
        this.resultsUsers = resultsUsers;
    }

    public User getLastSelectedUser() {
        return lastSelectedUser;
    }

    public void setLastSelectedUser(User lastSelectedUser) {
        this.lastSelectedUser = lastSelectedUser;
    }
}
