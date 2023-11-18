package main.java.ee.playtech.casino.services;

import main.java.ee.playtech.casino.models.Match;
import main.java.ee.playtech.casino.models.PlayerAction;
import main.java.ee.playtech.casino.models.enums.BetSide;
import main.java.ee.playtech.casino.models.enums.MatchResult;
import main.java.ee.playtech.casino.models.enums.PlayerOperation;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class ParserService {
    public static Match parseMatch(String input){
        String[] parts = input.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid input format");
        }

        UUID matchId = UUID.fromString(parts[0]);
        BigDecimal aReturnRate = new BigDecimal(parts[1]);
        BigDecimal bReturnRate = new BigDecimal(parts[2]);
        MatchResult result = MatchResult.valueOf(parts[3]);

        return new Match(matchId, aReturnRate, bReturnRate, result);
    }

    public static PlayerAction parsePlayerAction(String input) {
        String[] parts = input.split(",",-1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid input format");
        }
        UUID playerId = UUID.fromString(parts[0]);
        PlayerOperation operation = PlayerOperation.valueOf(parts[1]);

        Optional<UUID> matchId;
        if (parts[2] == null || parts[2].isEmpty()) {
            matchId = Optional.empty();
        }
        else{
            UUID uuid = UUID.fromString(parts[2]);
            matchId = Optional.of(uuid);
        }

        int coinNumber = Integer.parseInt(parts[3]);

        Optional<BetSide> betSide;
        if (parts[4] == null || parts[4].isEmpty()) {
            betSide = Optional.empty();
        }
        else{
            betSide = Optional.of(BetSide.valueOf(parts[4]));
        }

        return new PlayerAction(playerId,operation,matchId,coinNumber,betSide);

    }
}
