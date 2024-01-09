package app.pages.Visitor;

import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.HostPage;
import app.pages.LikedContentPage;
import app.user.User;

public interface Visitor {
    int MAX = 5;

    /**
     * visit a likedcontentpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    StringBuilder visit(LikedContentPage page, User user);

    /**
     * visit an artistpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    StringBuilder visit(ArtistPage page, User user);

    /**
     * visit a hostpage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    StringBuilder visit(HostPage page, User user);

    /**
     * visit a homepage
     *
     * @param page the page
     * @param user the user
     * @return a string builder
     */
    StringBuilder visit(HomePage page, User user);
}
