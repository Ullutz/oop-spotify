package main;

import app.Admin;
import app.commandFactory.CommandFactory;
import app.commands.*;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

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
        System.out.println("\n\n" + filePath1 + "\n\n");
        CommandInput[] commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                        + "test04_etapa3_monetization_premium.json"),
                CommandInput[].class);
        ArrayNode outputs = objectMapper.createArrayNode();

        Admin.getInstance().setUser(library.getUsers());
        Admin.getInstance().setSong(library.getSongs());
        Admin.getInstance().setPodcast(library.getPodcasts());

        for (CommandInput command : commands) {
            Admin.getInstance().updateTimestamp(command.getTimestamp());

            String commandName = command.getCommand();

            Command concreteCommand = CommandFactory.createCommand(commandName);

            if (concreteCommand == null) {
                System.out.println("Unknown command");
            } else {
                outputs.add(concreteCommand.execute(command));
            }

        }

        outputs.add(new EndProgram().execute(new CommandInput()));

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        Admin.getInstance().reset();
    }
}
