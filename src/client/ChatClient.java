package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import network.Message;
import network.MessageType;
import user_interface.ClientDisplay;

public final class ChatClient {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String username;
    private final Socket serverSocket;
    private final ClientDisplay display;

    public ChatClient(final String serverIp, final int port, final String username,
                      final ClientDisplay display) throws IOException {
        this.username = username;
        this.serverSocket = new Socket(serverIp, port);
        this.display = display;

        dataInputStream = new DataInputStream(serverSocket.getInputStream());
        dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());

        sendToServer(username);

        new IncomingMessagesListener().start();

        // Inform the server of user disconnection.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    sendToServer("terminateme");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void sendToServer(final String message) {
        new Message(MessageType.TEXT, message).send(dataOutputStream);
    }

    private void messageReceived(final Message newMessage) throws IOException {
        this.display.newMessage(newMessage.getBody().toString());
    }

    private final class IncomingMessagesListener extends Thread {
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
}







