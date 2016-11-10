package drivers;

import java.io.IOException;

import server.ChatServer;

public class ServerDriver {

    private static final int PORT = 47360;

    public static void main(String args[]) throws IOException {
        new ChatServer(PORT);

        System.out.println(String.format("Server is up and running on port %d.", PORT));
    }
}
