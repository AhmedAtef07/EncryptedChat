package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import network.Message;
import network.MessageType;

class ConnectedClient extends Thread {
    private final ChatServer chatServer;
    private final String username;

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public ConnectedClient(final Socket clientSocket, final ChatServer chatServer)
            throws IOException {
        this.chatServer = chatServer;

        this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
        this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        // This is the first thing agreed with the client to send.
        Message usernameMessage = new Message(dataInputStream);
        this.username = usernameMessage.getBody().toString();

        // Listen to any incoming messages on an external
        start();
    }

    public void sendMessage(final String message) throws IOException {
        Message textMessage = new Message(MessageType.TEXT, message);
        textMessage.send(dataOutputStream);
    }

    private void messageReceived(final Message newMessage) throws IOException {
        chatServer.broadcast(username, newMessage.getBody().toString());
    }

    @Override
    public void run() {
        try {
            while (true) {
                messageReceived(new Message(dataInputStream));
            }
        } catch (Exception ex) {
        }
    }
}
