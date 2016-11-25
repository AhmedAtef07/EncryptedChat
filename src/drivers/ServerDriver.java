package drivers;

import java.io.IOException;
import java.util.Scanner;

import server.ChatServer;

public class ServerDriver {

    private static final int PORT = 47360;

    public static void main(String args[]) throws IOException {
        ChatServer chatServer = new ChatServer(PORT);

        System.out.println(String.format("Server is up and running on port %d.", PORT));

        System.out.println("Insert anything anytime to print a list of connected clients.");
        while (true) {
            new Scanner(System.in).next();
            System.out.println(chatServer.getConnectedClientNames());
        }
    }
}
