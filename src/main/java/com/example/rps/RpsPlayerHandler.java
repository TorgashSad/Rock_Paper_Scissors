package com.example.rps;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class RpsPlayerHandler implements Runnable {

    private final Matchmaker matchmaker;
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
            matchmaker.registerPlayer(player);
            Lobby lobby = player.getLobbyWhenAssigned();
            writer.println(STR."Your opponent is found! Name: \{lobby.getOpponent(player).getPlayerName()}. Let the battle start! Enter 'Rock', 'Paper' or 'Scissors'.");
            boolean moveAccepted;
            do {
                String playerMove = reader.readLine();
                moveAccepted = lobby.acceptAnswer(player, playerMove);
                if (!moveAccepted) {
                    writer.println(STR."Move is not allowed: \{playerMove}. Enter only either 'Rock', 'Paper' or 'Scissors'.");
                }
            } while (!moveAccepted);
            writer.println("Your move is accepted!");
            player.setWaitingForTheOpponentsMove();
            //TODO: resolveBattle
            writer.println("The battle result is awesome :O");
            player.closeSocket();
            RpsServer.removeClientConnection(player.getPlayerId()); // Remove client connection from the map
//            String inputLine;
//            while ((inputLine = reader.readLine()) != null) {
//                // Echo back the input to the client
//                writer.println(STR."You said: \{inputLine}");
//            }
            // Add logic for game matching, handling player choices, etc.
        } catch (IOException e) {
            System.err.println(STR."Error handling client connection: \{e.getMessage()}");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

