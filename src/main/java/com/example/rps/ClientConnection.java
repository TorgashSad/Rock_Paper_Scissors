package com.example.rps;

import java.net.Socket;

public record ClientConnection(Socket clientSocket, Thread clientThread) {

}
