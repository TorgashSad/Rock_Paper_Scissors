package com.example.rps;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RpsServer {

    private static final int PORT = 8888; // Choose a port number
    //TODO: I don't need it?
    private static final Map<Integer, Player> playersPool = new ConcurrentHashMap<>();
    private static final Matchmaker matchmaker = new Matchmaker();

    @PostConstruct
    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(STR."Server started on port \{PORT}");
            while (true) {
                // Accept incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println(STR."New client connected: \{clientSocket.getInetAddress()}");

                Player newPlayer = new Player(clientSocket);
                // Start a new thread to handle the client
                Thread clientThread = new Thread(new RpsPlayerHandler(matchmaker, newPlayer));
                clientThread.start();

                // Store the client connection
                playersPool.put(newPlayer.getPlayerId(), newPlayer);
            }
        } catch (IOException e) {
            System.err.println(STR."Error starting server: \{e.getMessage()}");
        }
    }

    // Method to remove a client connection when it's closed
    public static void removeClientConnection(int playerId) {
        playersPool.remove(playerId);
        Player.returnPlayerIdToThePool(playerId); // Return playerId to the pool for reuse
    }
}



