package app.Pages;

import app.user.NormalUser;
import app.user.User;

public interface Page {
    public StringBuilder printCurrentPage(User user);

    public void updatePage(User user);

    public String getOwner();
    public void setOwner(String username);
}
