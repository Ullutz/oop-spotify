package app.pages;

import app.pages.Visitor.Visitable;
import app.pages.Visitor.Visitor;
import app.user.User;

public interface Page extends Visitable {

    /**
     * accepts the visitor
     *
     * @param visitor the visitor
     * @param user the user
     * @return a string builder
     */
    @Override
    StringBuilder accept(Visitor visitor, User user);

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

    /**
     * method to be overwritten by the implementations of
     * this interface
     *
     * @return null
     */
    default String getType() {
        return null;
    }
}
