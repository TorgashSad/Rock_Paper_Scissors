package com.example.rps;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class RpsPlayerHandler implements Runnable {

    private final Socket playerSocket;
    private final Player player;

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(playerSocket.getOutputStream(), true)
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
                playerSocket.close();
                RpsServer.removeClientConnection(player.getPlayerId()); // Remove client connection from the map
            } catch (IOException e) {
                System.err.println(STR."Error closing client socket: \{e.getMessage()}");
            }
        }
    }
}

