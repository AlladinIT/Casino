package main.java.ee.playtech.casino.services;


import main.java.ee.playtech.casino.models.LegitimatePlayer;
import main.java.ee.playtech.casino.models.Match;
import main.java.ee.playtech.casino.models.PlayerAction;
import main.java.ee.playtech.casino.utils.Helper;

import java.math.BigDecimal;
import java.util.*;

public class BettingDataProcessorService {
    public void run() {
        FileService fileService = new FileService();
        List<Match> listOfMatches = fileService.readMatches();
        if (listOfMatches.isEmpty()){
            return; // Error while reading/parsing file
        }
        List<PlayerAction> playerActionList = fileService.readPlayerActions();
        if (playerActionList.isEmpty()){
            return; // Error while reading/parsing file
        }


        Map<UUID,List<PlayerAction>> groupedByPlayerId = Helper.groupActionsByPlayerId(playerActionList);
        Map<UUID,Match> matchMap = Helper.convertListToMap(listOfMatches);

        List<LegitimatePlayer> legitimatePlayers = new ArrayList<>();
        List<PlayerAction> illegitimateActions = new ArrayList<>();
        long casinoBalance = 0;

        for (Map.Entry<UUID, List<PlayerAction>> entry : groupedByPlayerId.entrySet()){

            UUID playerId = entry.getKey();
            List<PlayerAction> actions = entry.getValue();

            PlayerAction res = BrainService.getIllegitimateAction(actions, matchMap);
            if (res == null){
                long accountBalance = BrainService.getAccountBalance();
                BigDecimal winRate = BrainService.getWinRate();
                legitimatePlayers.add(new LegitimatePlayer(playerId, accountBalance, winRate));

                casinoBalance = casinoBalance + BrainService.getCasinoBalance();

            }
            else{
                illegitimateActions.add(res);
            }
        }

        legitimatePlayers.sort(Comparator.comparing(LegitimatePlayer::getPlayerId));
        illegitimateActions.sort(Comparator.comparing(PlayerAction::getPlayerId));
        FileService.writeOutput(legitimatePlayers, illegitimateActions, casinoBalance);


    }
}
