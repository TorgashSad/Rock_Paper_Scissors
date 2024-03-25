package com.example.rps;

public class Lobby {
    private final Player player1;
    private final Player player2;

    public Lobby(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getOpponent(Player player) {
        return player.equals(player1) ? player2 : player1;
    }

    // Additional lobby-related methods and logic can be added here
}
