package main.java.ee.playtech.casino.services;

import main.java.ee.playtech.casino.models.Match;
import main.java.ee.playtech.casino.models.PlayerAction;
import main.java.ee.playtech.casino.models.enums.BetSide;
import main.java.ee.playtech.casino.models.enums.MatchResult;
import main.java.ee.playtech.casino.models.enums.PlayerOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BrainService {
    private static long accountBalance;
    private static BigDecimal winRate;
    private static long casinoBalance;

    public static PlayerAction getIllegitimateAction(List<PlayerAction> actions, Map<UUID,Match> matchMap) {

        accountBalance = 0;
        winRate = BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP);
        casinoBalance = 0;
        BigDecimal numberOfPlacedBets = BigDecimal.ZERO;
        BigDecimal numberOfWonGames = BigDecimal.ZERO;
        Set<UUID> matchIdsUserPlacedBetBefore = new HashSet<>();

        for (PlayerAction action: actions) {

            if (actionIsInvalid(action)){
                return action;
            }

            if (action.getOperation().equals(PlayerOperation.DEPOSIT)){
                accountBalance = accountBalance + action.getCoinNumber(); //add money to balance
            }

            else if (action.getOperation().equals(PlayerOperation.BET)){
                if (accountBalance < action.getCoinNumber()){
                    System.out.println("User tried to bet more money than he had: " + action);
                    return action; //user tried to bet more money than he had
                }

                UUID matchIdUserPlacedBet = action.getMatchId().orElseThrow(() -> new NoSuchElementException("No value present"));
                BetSide sideUserPlacedBet = action.getBetSide().orElseThrow(() -> new NoSuchElementException("No value present"));

                Match currentMatch = matchMap.get(matchIdUserPlacedBet);
                if (currentMatch == null){
                    System.out.println("User placed bet on nonexistent match: " + action);
                    return action; //User placed bet on nonexistent match
                }
                if (matchIdsUserPlacedBetBefore.contains(matchIdUserPlacedBet)){
                    System.out.println("User already placed bet on this match: " + action);
                    return action; //User already placed bet on this match
                }

                accountBalance = accountBalance - action.getCoinNumber(); //bet is made
                casinoBalance = casinoBalance + action.getCoinNumber();
                matchIdsUserPlacedBetBefore.add(matchIdUserPlacedBet);
                numberOfPlacedBets = numberOfPlacedBets.add(BigDecimal.ONE);

                if (currentMatch.getResult().equals(MatchResult.DRAW)){
                    accountBalance = accountBalance + action.getCoinNumber();
                    casinoBalance = casinoBalance - action.getCoinNumber();
                }
                else if(currentMatch.getResult().name().equals(sideUserPlacedBet.name())){
                    BigDecimal correspondingSideRate = getCorrespondingSideRate(currentMatch);
                    int winning = correspondingSideRate.multiply(new BigDecimal(action.getCoinNumber())).intValue();
                    accountBalance = accountBalance + action.getCoinNumber() + winning;
                    casinoBalance = casinoBalance - action.getCoinNumber() - winning;
                    numberOfWonGames = numberOfWonGames.add(BigDecimal.ONE);
                }

            }

            else if (action.getOperation().equals(PlayerOperation.WITHDRAW)){
                if (accountBalance < action.getCoinNumber()){
                    System.out.println("User tried to withdraw more money than he had: " + action);
                    return action; //user tried to withdraw more money than he had
                }
                accountBalance = accountBalance - action.getCoinNumber();
            }
            else {
                return action;
            }
        }

        if (!numberOfPlacedBets.equals(BigDecimal.ZERO)){
            winRate = numberOfWonGames.divide(numberOfPlacedBets, 2,RoundingMode.HALF_UP);
        }
        return null;
    }


    private static BigDecimal getCorrespondingSideRate(Match currentMatch) {
        if (currentMatch.getResult().equals(MatchResult.A)){
            return currentMatch.getaReturnRate();
        }
        else{
            return currentMatch.getbReturnRate();
        }
    }


    private static boolean actionIsInvalid(PlayerAction action) {

        if (action.getCoinNumber() <= 0){
            System.out.println("User used negative value or 0 in coinNumber : " + action);
            return true;
        }

        if (action.getOperation().equals(PlayerOperation.BET) &&
                (action.getMatchId().isEmpty() ||
                action.getBetSide().isEmpty())){
            System.out.println("User tried to BET without specifying matchId or betSide: " + action);
            return true;
        }
        if ((action.getOperation().equals(PlayerOperation.WITHDRAW) ||
                action.getOperation().equals(PlayerOperation.DEPOSIT)) &&
                (action.getMatchId().isPresent() ||
                        action.getBetSide().isPresent())){
            System.out.println("User tried to withdraw or deposit and specified matchId or betSide: " + action);
            return true;
        }


        return false;
    }

    public static long getAccountBalance() {
        return accountBalance;
    }
    public static BigDecimal getWinRate() {
        return winRate;
    }
    public static long getCasinoBalance() {
        return casinoBalance;
    }
}
