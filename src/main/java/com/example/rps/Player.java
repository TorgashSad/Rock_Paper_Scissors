package com.example.rps;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    private static final ConcurrentLinkedQueue<Integer> playerIdPool = new ConcurrentLinkedQueue<>();
    private static int playerIdCounter = 0;

    @EqualsAndHashCode.Include
    private final int playerId;
    private final Socket playerSocket;
    @Setter
    private String playerName;
    private Lobby lobby;
    private final CountDownLatch latch = new CountDownLatch(1);

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        latch.countDown();
    }

    public Player(Socket playerSocket) {
        this.playerId = selectPlayerId();
        this.playerSocket = playerSocket;
    }

    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
    }

    public PrintWriter getPrintWriter() throws IOException {
        return new PrintWriter(playerSocket.getOutputStream(), true);
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

    public void closeSocket() throws IOException {
        playerSocket.close();
    }

    public Lobby getLobbyWhenAssigned() throws InterruptedException {
        latch.await();
        return lobby;
    }

    public static void returnPlayerIdToThePool(int playerId) {
        playerIdPool.offer(playerId);
    }
}
