package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                logConnectedClient(c);
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

    void broadcast(final byte[] data) {
        for (ConnectedClient connectedClient : clients) {
            try {
                connectedClient.sendMessage(data);
            } catch (IOException e) {
                // Stop sending this client any future messages.
                logDisconnectedClient(connectedClient);
                clients.remove(connectedClient);
            }
        }
    }

    private void logConnectedClient(ConnectedClient connectedClient) {
        System.out.println(String.format("New client connected with name [%s].",
                connectedClient.getName()));
    }

    private void logDisconnectedClient(ConnectedClient connectedClient) {
        System.out.println(String.format("Client [%s] is no longer reachable and was removed " +
                "from the connected clients.", connectedClient.getName()));
    }

    public String getConnectedClientNames() {
        List<String> connectedClientsNames = clients.stream()
                .map(ConnectedClient::getName)
                .collect(Collectors.toList());
        return Arrays.toString(connectedClientsNames.toArray());
    }
}
