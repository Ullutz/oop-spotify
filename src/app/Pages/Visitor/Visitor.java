package app.Pages.Visitor;

import app.Pages.ArtistPage;
import app.Pages.HomePage;
import app.Pages.HostPage;
import app.Pages.LikedContentPage;
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
