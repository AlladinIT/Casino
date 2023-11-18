package main.java.ee.playtech.casino.models;

import main.java.ee.playtech.casino.models.enums.BetSide;
import main.java.ee.playtech.casino.models.enums.PlayerOperation;

import java.util.Optional;
import java.util.UUID;

public class PlayerAction {
    private UUID playerId;
    private Enum<PlayerOperation> operation;
    private Optional<UUID> matchId;
    private int coinNumber;
    private Optional<BetSide> betSide;

    public PlayerAction(UUID playerId, Enum<PlayerOperation> operation, Optional<UUID> matchId, int coinNumber, Optional<BetSide> betSide) {
        this.playerId = playerId;
        this.operation = operation;
        this.matchId = matchId;
        this.coinNumber = coinNumber;
        this.betSide = betSide;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Enum<PlayerOperation> getOperation() {
        return operation;
    }

    public Optional<UUID> getMatchId() {
        return matchId;
    }

    public int getCoinNumber() {
        return coinNumber;
    }

    public Optional<BetSide> getBetSide() {
        return betSide;
    }

    @Override
    public String toString() {
        return playerId + " "
                + operation + " "
                + matchId.map(UUID::toString).orElse("null") + " "
                + coinNumber+ " "
                + betSide.map(BetSide::toString).orElse("null") + "\n";
    }
}
