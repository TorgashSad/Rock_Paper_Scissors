package com.example.rps;

import lombok.Getter;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class Player {
    private static final ConcurrentLinkedQueue<Integer> playerIdPool = new ConcurrentLinkedQueue<>();
    private static int playerIdCounter = 0;

    private final int playerId;
    private final Socket playerSocket;

    public Player(Socket playerSocket) {
        this.playerId = selectPlayerId();
        this.playerSocket = playerSocket;
    }

    private static int selectPlayerId() {
        int playerId;
        if (!playerIdPool.isEmpty()) {
            playerId = playerIdPool.poll();
        } else {
            playerId = playerIdCounter++;
        }
        return playerId;
    }

    public static void returnPlayerIdToThePool(int playerId) {
        playerIdPool.offer(playerId);
    }
}
