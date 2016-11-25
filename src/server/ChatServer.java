package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import security.Credentials;
import security.EncryptionAlgorithm;

public class ChatServer extends Thread {

    private final Set<ConnectedClient> clients;
    private final ServerSocket server;
    private final Credentials credentials;
    private final SecretKey desKey;

    public ChatServer(final int port) throws IOException {
        this.clients = Collections.synchronizedSet(new HashSet<>());
        this.server = new ServerSocket(port);
        this.credentials = new Credentials(EncryptionAlgorithm.RSA);
        this.desKey = Credentials.generateDesKey();

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

    SecretKey getDesKey() {
        return desKey;
    }

    void broadcast(final byte[] data) throws IOException {
        System.out.println("Broadcasting => " + data.length);
        for (ConnectedClient connectedClient : clients) {
            connectedClient.sendMessage(data);
        }
    }
}
