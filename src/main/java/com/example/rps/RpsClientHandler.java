package com.example.rps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RpsClientHandler implements Runnable {

    private final Socket clientSocket;
    private final int clientId;

    public RpsClientHandler(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Handle client communication here
            writer.println("Welcome to Rock-Paper-Scissors game! Please wait for opponent.");
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // Echo back the input to the client
                writer.println(STR."You said: \{inputLine}");
            }
            // Add logic for game matching, handling player choices, etc.
        } catch (IOException e) {
            System.err.println(STR."Error handling client connection: \{e.getMessage()}");
        } finally {
            try {
                clientSocket.close();
                RpsServer.removeClientConnection(clientId); // Remove client connection from the map
            } catch (IOException e) {
                System.err.println(STR."Error closing client socket: \{e.getMessage()}");
            }
        }
    }
}

