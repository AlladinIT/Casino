package main.java.ee.playtech.casino.models;

import java.math.BigDecimal;
import java.util.UUID;

public class LegitimatePlayer {
    private UUID playerId;
    private long accountBalance;
    private BigDecimal winRate;

    public LegitimatePlayer(UUID playerId, long accountBalance, BigDecimal winRate) {
        this.playerId = playerId;
        this.accountBalance = accountBalance;
        this.winRate = winRate;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return playerId + " "
                + accountBalance + " "
                + winRate + "\n";
    }
}
