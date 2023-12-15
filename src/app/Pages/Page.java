package app.Pages;

import app.user.User;

public interface Page {
    /**
     * prints the current page
     *
     * @param user the user
     * @return a stringbuilder
     */
    StringBuilder printCurrentPage(User user);

    /**
     * updates the elemts of the page
     *
     * @param user the user
     */
    void updatePage(User user);

    /**
     * gets the owner of the page
     *
     * @return the owner
     */
    String getOwner();

    /**
     * sets the owner os the page
     *
     * @param username the username
     */
    void setOwner(String username);
}
