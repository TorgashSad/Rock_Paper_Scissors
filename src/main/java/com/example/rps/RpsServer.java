package com.example.rps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RpsServer {

    private static final int PORT = 8888; // Choose a port number

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                // Accept incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Start a new thread to handle the client
                Thread clientThread = new Thread(new RpsClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}



