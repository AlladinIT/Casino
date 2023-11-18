package main.java.ee.playtech.casino.models;

import main.java.ee.playtech.casino.models.enums.MatchResult;

import java.math.BigDecimal;
import java.util.UUID;

public class Match {
    private UUID matchId;
    private BigDecimal aReturnRate;
    private BigDecimal bReturnRate;
    private Enum<MatchResult> result;

    public Match(UUID matchId, BigDecimal aReturnRate, BigDecimal bReturnRate, Enum<MatchResult> result) {
        this.matchId = matchId;
        this.aReturnRate = aReturnRate;
        this.bReturnRate = bReturnRate;
        this.result = result;
    }


    public UUID getMatchId() {
        return matchId;
    }

    public BigDecimal getaReturnRate() {
        return aReturnRate;
    }

    public BigDecimal getbReturnRate() {
        return bReturnRate;
    }

    public Enum<MatchResult> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return matchId + " "
                + aReturnRate + " "
                + bReturnRate + " "
                + result;
    }
}
