package com.example.rps;

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RpsApplicationTests {

	@Test
	void simpleConnect() throws IOException {
		TelnetClient telnetClient = new TelnetClient();
		telnetClient.connect("192.168.1.8", 8888);
		assertTrue(telnetClient.isConnected());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));
		assertEquals("Welcome to Rock-Paper-Scissors game! Please enter your name.", bufferedReader.readLine());
		telnetClient.disconnect();
	}

}
