package com.example.rps.impl;

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
import java.util.concurrent.Semaphore;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    private static final ConcurrentLinkedQueue<Integer> playerIdPool = new ConcurrentLinkedQueue<>();
    private static int playerIdCounter = 0;

    @EqualsAndHashCode.Include
    private final int playerId;
    private final Socket playerSocket;
    private final CountDownLatch lobbyAssignedLatch = new CountDownLatch(1);
    private final Semaphore waitingForTheOpponentSemaphore = new Semaphore(0);
    @Setter
    private String playerName;
    private Lobby lobby;


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

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        lobbyAssignedLatch.countDown();
    }

    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
    }

    public PrintWriter getPrintWriter() throws IOException {
        return new PrintWriter(playerSocket.getOutputStream(), true);
    }

    public void closeSocket() throws IOException {
        playerSocket.close();
    }

    public Lobby getLobbyWhenAssigned() throws InterruptedException {
        lobbyAssignedLatch.await();
        return lobby;
    }

    public static void returnPlayerIdToThePool(int playerId) {
        playerIdPool.offer(playerId);
    }

    public void setWaitingForTheOpponentsMove() throws InterruptedException {
        waitingForTheOpponentSemaphore.acquire();
    }

    public void release() {
        waitingForTheOpponentSemaphore.release();
    }
}
