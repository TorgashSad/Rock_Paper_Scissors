package com.example.rps;

import java.util.Set;

public class Lobby {
    private static final String ROCK = "Rock";
    private static final String PAPER = "Paper";
    private static final String SCISSORS = "Scissors";
    private static final Set<String> ROCK_PAPER_SCISSORS = Set.of(ROCK, PAPER, SCISSORS);
    private final Player firstPlayer;
    private final Player secondPlayer;

    private String firstPlayerMove;
    private String secondPlayerMove;

    private BattleResult battleResult;

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
            battleResult = resolveBattle();
            player.release();
            getOpponent(player).release();
        }
        return true;
    }

    private BattleResult resolveBattle() {
        if (firstPlayerMove.equals(secondPlayerMove)) {
            return BattleResult.DRAW;
        } else {
            return switch (firstPlayerMove) {
                case ROCK ->
                        (secondPlayerMove.equals(SCISSORS)) ? BattleResult.FIRST_PLAYER_WINS : BattleResult.SECOND_PLAYER_WINS;
                case PAPER ->
                        (secondPlayerMove.equals(ROCK)) ? BattleResult.FIRST_PLAYER_WINS : BattleResult.SECOND_PLAYER_WINS;
                case SCISSORS ->
                        (secondPlayerMove.equals(PAPER)) ? BattleResult.FIRST_PLAYER_WINS : BattleResult.SECOND_PLAYER_WINS;
                default ->
                        throw new IllegalArgumentException(STR."Invalid moves: \{firstPlayerMove}, \{secondPlayerMove}");
            };
        }
    }

    private boolean validateMove(String move) {
        return ROCK_PAPER_SCISSORS.contains(move);
    }

    public Boolean getBattleResult(Player player) {
        if (player.equals(firstPlayer)) {
            return switch (battleResult) {
                case FIRST_PLAYER_WINS -> true;
                case SECOND_PLAYER_WINS -> false;
                default -> null; // Draw
            };
        } else if (player.equals(secondPlayer)) {
            return switch (battleResult) {
                case FIRST_PLAYER_WINS -> false;
                case SECOND_PLAYER_WINS -> true;
                default -> null; // Draw
            };
        } else {
            throw new IllegalArgumentException("Player is not in this lobby");
        }
    }

    public void resetMoves() {
        firstPlayerMove = null;
        secondPlayerMove = null;
    }

    public enum BattleResult {
        FIRST_PLAYER_WINS,
        SECOND_PLAYER_WINS,
        DRAW
    }
}
