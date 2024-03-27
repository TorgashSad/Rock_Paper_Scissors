package com.example.rps;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RpsApplicationTests {

	@Test
	void lobbyFormed() throws IOException {
		MyTelnetClient telnetClient1 = connectToServer();
		MyTelnetClient telnetClient2 = connectToServer();
		String testName1 = "TestName1";
		enterName(telnetClient1, testName1);
		String testName2 = "TestName2";
		enterName(telnetClient2, testName2);
		verifyLobbyCreated(telnetClient1, telnetClient2, testName1, testName2);
		telnetClient1.disconnect();
		telnetClient2.disconnect();
	}

	@Test
	void nClientsConnected() throws IOException {
		List<MyTelnetClient> clientList = new ArrayList<>();
		int n = 100;
		for (int i = 0; i < n; i++) {
			clientList.add(connectToServer());
		}
		clientList.forEach(myTelnetClient -> {
            try {
                myTelnetClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
	}

	private void verifyLobbyCreated(MyTelnetClient telnetClient1, MyTelnetClient telnetClient2, String playerName1, String playerName2) throws IOException {
		BufferedReader bufferedReader1 = telnetClient1.getReader();
		String actualServerResponse1 = bufferedReader1.readLine();
		assertEquals(STR."Your opponent is found! Name: \{playerName2}. Let the battle start! Enter 'Rock', 'Paper' or 'Scissors'.", actualServerResponse1);
		BufferedReader bufferedReader2 = telnetClient2.getReader();
		String actualServerResponse2 = bufferedReader2.readLine();
		assertEquals(STR."Your opponent is found! Name: \{playerName1}. Let the battle start! Enter 'Rock', 'Paper' or 'Scissors'.", actualServerResponse2);
	}

	private void enterName(MyTelnetClient telnetClient, String playerName) throws IOException {
		PrintWriter printWriter = telnetClient.getWriter();
		printWriter.println(playerName);
		BufferedReader bufferedReader = telnetClient.getReader();
		assertEquals(STR."Your name is: \{playerName}. Looking for an opponent...", bufferedReader.readLine());
	}

	private MyTelnetClient connectToServer() throws IOException {
		TelnetClient telnetClient = new TelnetClient();
		telnetClient.connect(InetAddress.getLocalHost(), 8888);
		MyTelnetClient myTelnetClient = new MyTelnetClient(telnetClient);
		assertTrue(myTelnetClient.isConnected());
		BufferedReader bufferedReader = myTelnetClient.getReader();
		assertEquals("Welcome to Rock-Paper-Scissors game! Please enter your name.", bufferedReader.readLine());
		return myTelnetClient;
	}

	@Getter
	private static class MyTelnetClient {

		@Delegate
		private final TelnetClient telnetClient;
		private final BufferedReader reader;
		private final PrintWriter writer;

		private MyTelnetClient(TelnetClient telnetClient) {
			this.telnetClient = telnetClient;
			this.reader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));
			this.writer = new PrintWriter(telnetClient.getOutputStream(), true);
		}
	}

}
