package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import security.Credentials;
import security.EncryptionAlgorithm;

public class ChatServer extends Thread {

    private final Set<ConnectedClient> clients;
    private final ServerSocket server;
    private final Credentials credentials;

    public ChatServer(final int port) throws IOException {
        this.clients = Collections.synchronizedSet(new HashSet<>());
        this.server = new ServerSocket(port);
        this.credentials = new Credentials(EncryptionAlgorithm.RSA);

        // Start waiting for clients to connect on detached thread.
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = server.accept();
                ConnectedClient c = new ConnectedClient(clientSocket, this);
                clients.add(c);
            }
        } catch (Exception ex) {
        }
    }

    Credentials getCredentials() {
        return credentials;
    }

    void broadcast(final String username, final String textMessage) throws IOException {
        System.out.println("Broadcasting => " + (makeMessage(username, textMessage)));
        for (ConnectedClient connectedClient : clients) {
            connectedClient.sendMessage(makeMessage(username, textMessage));
        }
    }

    private String makeMessage(final String username, final String textMessage) {
        return String.format("%s: %s", username, textMessage);
    }
}
