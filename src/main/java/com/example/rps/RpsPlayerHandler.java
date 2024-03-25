package com.example.rps;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class RpsPlayerHandler implements Runnable {

    private final Player player;

    @Override
    public void run() {
        try (
                BufferedReader reader = player.getBufferedReader();
                PrintWriter writer = player.getPrintWriter()
        ) {
            // Handle client communication here
            writer.println("Welcome to Rock-Paper-Scissors game! Please enter your name.");
            String playerName = reader.readLine();
            player.setPlayerName(playerName);
            writer.println(STR."Your name is: \{playerName}. Looking for an opponent...");
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
                player.closeSocket();
                RpsServer.removeClientConnection(player.getPlayerId()); // Remove client connection from the map
            } catch (IOException e) {
                System.err.println(STR."Error closing client socket: \{e.getMessage()}");
            }
        }
    }
}

