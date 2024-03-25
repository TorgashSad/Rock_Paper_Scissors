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
            // Repeat the game until a decisive result is achieved
            Boolean battleResult;
            do {
                String playerMove;
                boolean moveAccepted;
                do {
                    playerMove = reader.readLine();
                    moveAccepted = lobby.acceptAnswer(player, playerMove);
                    if (!moveAccepted) {
                        writer.println("Move is not allowed: " + playerMove + ". Enter only either 'Rock', 'Paper' or 'Scissors'.");
                    }
                } while (!moveAccepted);
                writer.println("Your move is accepted!");
                player.setWaitingForTheOpponentsMove();
                writer.println("Waiting for the opponent's move...");

                // Get the battle result
                battleResult = lobby.getBattleResult(player);
                if (battleResult == null) {
                    lobby.resetMoves();
                    // Replay the game if the result is a draw
                    writer.println("The battle resulted in a draw. Let's replay!");
                    writer.println("Enter 'Rock', 'Paper' or 'Scissors' again.");
                }
            } while (battleResult == null);
            if (battleResult) {
                writer.println("YOU'VE WON THE BATTLE");
            } else {
                writer.println("you've lost the battle :'(");
            }
            disconnectPlayer();
        } catch (IOException e) {
            System.err.println(STR."Error handling client connection: \{e.getMessage()}");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                disconnectPlayer();
            } catch (IOException e) {
                System.err.println(STR."Error closing client socket: \{e.getMessage()}");
            }
        }
    }

    private void disconnectPlayer() throws IOException {
        player.closeSocket();
        RpsServer.removeClientConnection(player.getPlayerId()); // Remove client connection from the map
    }
}

