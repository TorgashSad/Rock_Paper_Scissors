package com.example.rps;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;

public class Lobby {
    private static final Set<String> ROCK_PAPER_SCISSORS = Set.of("Rock", "Paper", "Scissors");
    private final Player firstPlayer;
    private final Player secondPlayer;

    private String firstPlayerMove;
    private String secondPlayerMove;

    @Getter
    private Objects battleResult;

    public Lobby(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Player getOpponent(Player player) {
        return player.equals(firstPlayer) ? secondPlayer : firstPlayer;
    }

    public boolean acceptAnswer(Player player, String move) throws InterruptedException {
        if (!validateMove(move)) {
            return false;
        }
        if (firstPlayer.equals(player)) {
            firstPlayerMove = move;
        } else {
            secondPlayerMove = move;
        }
        if (firstPlayerMove != null && secondPlayerMove != null) {
            battleResult = null; //resolveBattle();
            player.release();
            getOpponent(player).release();
        }
        return true;
    }

    private boolean validateMove(String move) {
        return ROCK_PAPER_SCISSORS.contains(move);
    }
}
