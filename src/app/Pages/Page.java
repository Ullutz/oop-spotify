package app.Pages;

import app.Pages.Visitor.Visitable;
import app.Pages.Visitor.Visitor;
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
}
