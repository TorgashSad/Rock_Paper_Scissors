package com.example.rps;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Service
@RequiredArgsConstructor
public class RpsServer {

    @Value("${spring.application.server.port}")
    private int PORT; // Choose a port number
    private final Matchmaker matchmaker;

    @PostConstruct
    public void startServer() {
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
            }
        } catch (IOException e) {
            System.err.println(STR."Error starting server: \{e.getMessage()}");
        }
    }
}



