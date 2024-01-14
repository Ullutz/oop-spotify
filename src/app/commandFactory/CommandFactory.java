package app.commandFactory;

import app.commands.*;

public class CommandFactory {
    public static Command createCommand(final String commandName) {
        switch (commandName) {
            case "search": return new SearchCommand();
            case "select": return new SelectCommand();
            case "load" : return new LoadCommand();
            case "playPause": return new PlayPauseCommand();
            case "repeat" : return new RepeatCommand();
            case "shuffle" : return new ShuffleCommand();
            case "forward" : return new ForwardCommand();
            case "backward" : return new BackwardCommand();
            case "like" : return new LikeCommand();
            case "next" : return new NextCommand();
            case "prev" : return new PrevCommand();
            case "createPlaylist" : return new CreatePlaylistCommand();
            case "addRemoveInPlaylist" : return new AddInPlaylistCommand();
            case "switchVisibility" : return new SwitchVisibilityCommand();
            case "showPlaylists" : return new ShowPlaylistsCommand();
            case "follow" : return new FollowCommand();
            case "status" : return new GetStatusCommand();
            case "showPreferredSongs" : return new ShowLikedSongsCommand();
            case "getPreferredGenre" : {
                // outputs.add((new ShowPrefGenreCommand()).execute(command));
            }
            case "getTop5Songs" : return new GetTop5SongsCommand();
            case "getTop5Playlists" : return new GetTop5PlaylistsCommand();
            case "switchConnectionStatus" : return new SwitchConnectionStatusCommand();
            case "getOnlineUsers" : return new GetOnlineUsersCommand();
            case "addUser" : return new AddUserCommand();
            case "getAllUsers" : return new GetAllUsersCommand();
            case "addAlbum" : return new AddAlbumCommand();
            case "showAlbums" : return new ShowAlbumCommand();
            case "printCurrentPage" : return new PrintCurrPageCommand();
            case "addEvent" : return new AddEventCommand();
            case "removeEvent" : return new RemoveEventCommand();
            case "addMerch" : return new AddMerchCommand();
            case "deleteUser" : return new DeleteUserCommand();
            case "addPodcast" : return new AddPodcastCommand();
            case "addAnnouncement" : return new AddAnnouncementCommand();
            case "removeAnnouncement" : return new RemoveAnnouncementCommand();
            case "showPodcasts" : return new ShowPodcastsCommand();
            case "changePage" :  return new ChangePageCommand();
            case "removeAlbum" : return new RemoveAlbumCommand();
            case "removePodcast" : return new RemovePodcastCommand();
            case "getTop5Artists" : return new GetTop5ArtistsCommand();
            case "getTop5Albums" : return new GetTop5AlbumsCommand();
            case "wrapped" : return new WrappedCommand();
            case "subscribe" : return new SubscribeCommand();
            case "getNotifications" : return new GetNotificastionsCommand();
            case "buyMerch" : return new BuyMerchCommand();
            case "seeMerch" : return new SeeMyMerchCommand();
            case "nextPage" : return new NextPageCommand();
            case "previousPage" : return new PrevPageCommand();
            case "updateRecommendations" : return new UpdateRecommendationsCommand();
            case "loadRecommendations" : return new LoadRecommendationCommand();
            case "buyPremium" : return new BuyPremiumCommand();
            case "cancelPremium" : return new CancelPremiumCommand();
            default : return null;
        }
    }
}
