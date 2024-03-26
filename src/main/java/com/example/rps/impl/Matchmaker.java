package com.example.rps.impl;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class Matchmaker {

    private final ConcurrentMap<Integer, Player> waitingPlayers = new ConcurrentHashMap<>();

    public synchronized void registerPlayer(Player player) {
        waitingPlayers.put(player.getPlayerId(), player);
        tryMatchPlayers();
    }

    private synchronized void tryMatchPlayers() {
        if (waitingPlayers.size() >= 2) {
            Player[] players = waitingPlayers.values().toArray(new Player[0]);
            Player player1 = players[0];
            Player player2 = players[1];
            waitingPlayers.remove(player1.getPlayerId());
            waitingPlayers.remove(player2.getPlayerId());
            createLobby(player1, player2);
        }
    }

    private void createLobby(Player player1, Player player2) {
        Lobby lobby = new Lobby(player1, player2);
        player1.setLobby(lobby);
        player2.setLobby(lobby);
    }

}
