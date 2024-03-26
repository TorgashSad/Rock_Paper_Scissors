package com.example.rps;

import com.example.rps.impl.Matchmaker;
import com.example.rps.impl.Player;
import com.example.rps.impl.RpsPlayerHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class RpsServer {

    @Value("${spring.application.server.port}")
    private int PORT;
    private final Matchmaker matchmaker;
    // "Brian Goetz in his famous book "Java Concurrency in Practice" recommends the following formula:
    // Number of threads = Number of Available Cores * (1 + Wait time / Service time)
    // https://engineering.zalando.com/posts/2019/04/how-to-set-an-ideal-thread-pool-size.html
    // Virtual Threads in Java 21 - https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-BEC799E0-00E9-4386-B220-8839EA6B4F5C
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @PostConstruct
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(STR."Server started on port \{PORT}");
            while (true) {
                // Accept incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println(STR."New client connected: \{clientSocket.getInetAddress()}");
                Player newPlayer = new Player(clientSocket);
                executorService.execute(new RpsPlayerHandler(matchmaker, newPlayer));
            }
        } catch (IOException e) {
            System.err.println(STR."Error starting server: \{e.getMessage()}");
        }
    }
}



