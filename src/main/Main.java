package main;

import app.Admin;
import app.Commands.*;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import org.w3c.dom.html.HTMLImageElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                        + "library/library.json"),
                LibraryInput.class);
        CommandInput[] commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                        + "test06_etapa2_repeat.json"),
                CommandInput[].class);
        ArrayNode outputs = objectMapper.createArrayNode();

        Admin.getInstance().setUser(library.getUsers());
        Admin.getInstance().setSong(library.getSongs());
        Admin.getInstance().setPodcast(library.getPodcasts());

        for (CommandInput command : commands) {
            Admin.getInstance().updateTimestamp(command.getTimestamp());

            String commandName = command.getCommand();

            switch (commandName) {
                case "search" -> outputs.add((new SearchCommand()).execute(command));
                case "select" -> outputs.add((new SelectCommand()).execute(command));
                case "load" -> outputs.add((new LoadCommand()).execute(command));
                case "playPause" -> outputs.add((new PlayPauseCommand()).execute(command));
                case "repeat" -> outputs.add((new RepeatCommand()).execute(command));
                case "shuffle" -> outputs.add((new ShuffleCommand()).execute(command));
                case "forward" -> outputs.add((new ForwardCommand()).execute(command));
                case "backward" -> outputs.add((new BackwardCommand()).execute(command));
                case "like" -> outputs.add((new LikeCommand()).execute(command));
                case "next" -> outputs.add((new NextCommand()).execute(command));
                case "prev" -> outputs.add((new PrevCommand()).execute(command));
                case "createPlaylist" ->
                        outputs.add((new CreatePlaylistCommand()).execute(command));
                case "addRemoveInPlaylist" ->
                        outputs.add((new AddInPlaylistCommand()).execute(command));
                case "switchVisibility" ->
                        outputs.add((new SwitchVisibilityCommand()).execute(command));
                case "showPlaylists" ->
                        outputs.add((new ShowPlaylistsCommand()).execute(command));
                case "follow" -> outputs.add((new FollowCommand()).execute(command));
                case "status" -> outputs.add((new GetStatusCommand()).execute(command));
                case "showPreferredSongs" ->
                        outputs.add((new ShowLikedSongsCommand()).execute(command));
                case "getPreferredGenre" -> {
                    // outputs.add((new ShowPrefGenreCommand()).execute(command));
                }
                case "getTop5Songs" ->
                        outputs.add((new GetTop5SongsCommand()).execute(command));
                case "getTop5Playlists" ->
                        outputs.add((new GetTop5PlaylistsCommand()).execute(command));
                case "switchConnectionStatus" ->
                    outputs.add((new SwitchConnectionStatusCommand()).execute(command));
                case "getOnlineUsers" ->
                    outputs.add((new GetOnlineUsersCommand()).execute(command));
                case "addUser" ->
                    outputs.add((new AddUserCommand()).execute(command));
                case "getAllUsers" ->
                    outputs.add((new GetAllUsersCommand()).execute(command));
                case "addAlbum" ->
                    outputs.add((new AddAlbumCommand()).execute(command));
                case "showAlbums" ->
                    outputs.add(new ShowAlbumCommand().execute(command));
                case "printCurrentPage" ->
                    outputs.add(new PrintCurrPageCommand().execute(command));
                case "addEvent" ->
                    outputs.add(new AddEventCommand().execute(command));
                case "removeEvent" ->
                    outputs.add(new RemoveEventCommand().execute(command));
                case "addMerch" ->
                    outputs.add(new AddMerchCommand().execute(command));
                case "deleteUser" ->
                    outputs.add(new DeleteUserCommand().execute(command));
                case "addPodcast" ->
                    outputs.add(new AddPodcastCommand().execute(command));
                case "addAnnouncement" ->
                    outputs.add(new AddAnnouncementCommand().execute(command));
                case "removeAnnouncement" ->
                    outputs.add(new RemoveAnnouncementCommand().execute(command));
                case "showPodcasts" ->
                    outputs.add(new ShowPodcastsCommand().execute(command));
                case "changePage" ->
                    outputs.add(new ChangePageCommand().execute(command));
                case "removeAlbum" ->
                    outputs.add(new RemoveAlbumCommand().execute(command));
                default -> System.out.println("Invalid command " + commandName);
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        Admin.getInstance().reset();
    }
}
