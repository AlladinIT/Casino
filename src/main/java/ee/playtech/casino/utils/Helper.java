package main.java.ee.playtech.casino.utils;

import main.java.ee.playtech.casino.models.Match;
import main.java.ee.playtech.casino.models.PlayerAction;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Helper {

    public static Map<UUID, List<PlayerAction>> groupActionsByPlayerId(List<PlayerAction> playerActions) {
        return playerActions.stream()
                .collect(Collectors.groupingBy(PlayerAction::getPlayerId));
    }

    public static Map<UUID, Match> convertListToMap(List<Match> listOfMatches) {
        return listOfMatches.stream()
                .collect(Collectors.toMap(Match::getMatchId, Function.identity()));
    }
}
