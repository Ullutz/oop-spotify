package app.Commands;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.HostUser;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

public final class ShowPodcastsCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        class PodcastResult {
            private final String name;
            private final List<String> episodeNames;

            PodcastResult(final String name, final List<String> episodeNames) {
                this.name = name;
                this.episodeNames = episodeNames;
            }

            public String getName() {
                return name;
            }

            public List<String> getEpisodes() {
                return episodeNames;
            }
        }

        List<PodcastResult> result = new ArrayList<>();

        for (Podcast podcast : ((HostUser) user).getPodcasts()) {
            List<String> episodeNames = new ArrayList<>();

            for (Episode episode : podcast.getEpisodes()) {
                episodeNames.add(episode.getName());
            }

            result.add(new PodcastResult(podcast.getName(), episodeNames));
        }

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", commandInput.getCommand());
        obj.put("user", commandInput.getUsername());
        obj.put("timestamp", commandInput.getTimestamp());
        obj.put("result", objectMapper.valueToTree(result));

        return obj;
    }
}
