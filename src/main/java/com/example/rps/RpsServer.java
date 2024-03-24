package com.example.rps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RpsServer {

    private static final int PORT = 8888; // Choose a port number
    private static final Map<Integer, ClientConnection> clientConnections = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<Integer> clientIdPool = new ConcurrentLinkedQueue<>();
    private static int clientIdCounter = 0;

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println(STR."Server started on port \{PORT}");

            while (true) {
                // Accept incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println(STR."New client connected: \{clientSocket.getInetAddress()}");

                // Assign a clientId to the client
                int clientId = getClientId();
                // Start a new thread to handle the client
                Thread clientThread = new Thread(new RpsClientHandler(clientSocket, clientId));
                clientThread.start();

                // Store the client connection
                clientConnections.put(clientId, new ClientConnection(clientSocket, clientThread));
            }
        } catch (IOException e) {
            System.err.println(STR."Error starting server: \{e.getMessage()}");
        }
    }

    private static int getClientId() {
        int clientId;
        if (!clientIdPool.isEmpty()) {
            clientId = clientIdPool.poll();
        } else {
            clientId = clientIdCounter++;
        }
        return clientId;
    }

    // Method to remove a client connection when it's closed
    public static void removeClientConnection(int clientId) {
        clientConnections.remove(clientId);
        clientIdPool.offer(clientId); // Return clientId to the pool for reuse
    }
}



