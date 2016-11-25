package drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import client.ChatClient;
import user_interface.CommandLineDisplay;

public class ClientDriver {

    private static final int PORT = 47360;
    private static final String IP = "localhost";

    public static void main(String args[]) throws IOException, InvalidKeySpecException,
            NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Insert a username: ");
        String username = br.readLine();


        ChatClient chatClient = new ChatClient(IP, PORT, username, new CommandLineDisplay());

        while (true) {
            chatClient.sendTextToServer(br.readLine());
        }
    }
}
