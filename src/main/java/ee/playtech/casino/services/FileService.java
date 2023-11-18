package main.java.ee.playtech.casino.services;

import main.java.ee.playtech.casino.models.LegitimatePlayer;
import main.java.ee.playtech.casino.models.Match;
import main.java.ee.playtech.casino.models.PlayerAction;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileService {
    private static final Logger logger = Logger.getLogger(FileService.class.getName());
    private static final String RESOURCES_FILE_PATH = "src/main/resources/";
    private static final String MATCH_DATA_FILE_PATH = RESOURCES_FILE_PATH + "match_data.txt";
    private static final String PLAYER_DATA_FILE_PATH = RESOURCES_FILE_PATH + "player_data.txt";
    private static final String OUTPUT_FILE_PATH = "src/main/java/ee/playtech/casino/results.txt";


    public List<Match> readMatches() {
        List<Match> matches = new ArrayList<>();

        Path filePath = Paths.get(MATCH_DATA_FILE_PATH);

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                Match match = ParserService.parseMatch(line);
                matches.add(match);
            }

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Collections.emptyList();
        }

        return matches;
    }


    public List<PlayerAction> readPlayerActions() {
        List<PlayerAction> playerActions = new ArrayList<>();

        Path filePath = Paths.get(PLAYER_DATA_FILE_PATH);

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                PlayerAction playerAction = ParserService.parsePlayerAction(line);
                playerActions.add(playerAction);
            }

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Collections.emptyList();
        }

        return playerActions;
    }


    public static void writeOutput(List<LegitimatePlayer> legitimatePlayers, List<PlayerAction> illegitimateActions, long casinoBalance) {
        FileWriter writer = null;
        try{
            writer = new FileWriter(OUTPUT_FILE_PATH);

            if (legitimatePlayers.isEmpty()){
                writer.write("\n");
            }
            else{
                for (LegitimatePlayer player : legitimatePlayers){
                    writer.write(player.toString());
                }
            }
            writer.write("\n");

            if (illegitimateActions.isEmpty()){
                writer.write("\n");
            }
            else{
                for (PlayerAction action : illegitimateActions){
                    writer.write(action.toString());
                }
            }
            writer.write("\n");

            writer.write(Long.toString(casinoBalance));


        }
        catch (IOException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        finally {
            if (writer != null){
                try {
                    writer.close();
                }
                catch (IOException e){
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }
}
