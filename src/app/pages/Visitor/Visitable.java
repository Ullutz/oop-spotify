package app.pages.Visitor;

import app.user.User;

public interface Visitable {
    /**
     * accepts a visitor
     *
     * @param visitor the visitor
     * @param user the user
     * @return a stringbuilder
     */
    StringBuilder accept(Visitor visitor, User user);
}
