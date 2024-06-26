package com.example.rps.impl;

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
            writer.println("Welcome to Rock-Paper-Scissors game! Please enter your name.");
            String playerName = reader.readLine(); //blocking
            player.setPlayerName(playerName);
            writer.println(STR."Your name is: \{playerName}. Looking for an opponent...");
            matchmaker.registerPlayer(player);
            Lobby lobby = player.getLobbyWhenAssigned(); //blocking
            writer.println(STR."Your opponent is found! Name: \{lobby.getOpponent(player).getPlayerName()}. Let the battle start! Enter 'Rock', 'Paper' or 'Scissors'.");
            // Repeat the game until a decisive result is achieved
            Boolean battleResult;
            do {
                String playerMove;
                boolean moveAccepted;
                do {
                    playerMove = reader.readLine(); //blocking
                    moveAccepted = lobby.acceptAnswer(player, playerMove);
                    if (!moveAccepted) {
                        writer.println(STR."Move is not allowed: \{playerMove}. Enter only either 'Rock', 'Paper' or 'Scissors'.");
                    }
                } while (!moveAccepted);
                writer.println("Your move is accepted! Waiting for the opponent's move...");
                player.setWaitingForTheOpponentsMove(); //blocking
                battleResult = lobby.getBattleResult(player);
                if (battleResult == null) {
                    lobby.resetMoves();
                    writer.println("The battle resulted in a draw. Let's replay!");
                    writer.println("Enter 'Rock', 'Paper' or 'Scissors' again.");
                }
            } while (battleResult == null);
            if (battleResult) {
                writer.println("YOU'VE WON THE BATTLE");
            } else {
                writer.println("You've lost the battle :'(");
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
        Player.returnPlayerIdToThePool(player.getPlayerId());
    }
}

