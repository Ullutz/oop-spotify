package app.commands;

import app.Admin;
import app.user.ArtistUser;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.List;

public class EndProgram implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode execute(CommandInput commandInput) {
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("command", "endProgram");

        ObjectNode result = objectMapper.createObjectNode();

        List<User> artists = Admin.getInstance().setUpArtistRankings();

        for (User artistUser : artists) {
            ArtistUser artist = (ArtistUser) artistUser;

            ObjectNode artistInfo = objectMapper.createObjectNode();

            artistInfo.put("songRevenue", artist.getSongRevenue());
            artistInfo.put("merchRevenue", artist.getMerchRevenue());
            artistInfo.put("ranking", artist.getRanking());
            artistInfo.put("mostProfitableSong", artist.getMostProfitableSong());

            result.put(artist.getUsername(), artistInfo);
        }

        obj.put("result", result);
        return obj;
    }
}
