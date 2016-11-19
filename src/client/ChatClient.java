package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import network.Message;
import network.MessageOperations;
import network.MessageType;
import security.Credentials;
import security.EncryptionAlgorithm;
import user_interface.ClientDisplay;

public final class ChatClient {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String username;
    private final Socket serverSocket;
    private final ClientDisplay display;
    private final Credentials credentials;
//    private final Key serverPublicKey;

    public ChatClient(final String serverIp, final int port, final String username,
                      final ClientDisplay display) throws IOException,
            InvalidKeySpecException, NoSuchAlgorithmException {
        this.username = username;
        this.serverSocket = new Socket(serverIp, port);
        this.display = display;
        this.credentials = new Credentials(EncryptionAlgorithm.RSA);

        dataInputStream = new DataInputStream(serverSocket.getInputStream());
        dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());

        new IncomingMessagesListener().start();

        sendTextToServer("asD");

        // Things to be sent to the server. Orders matters.
//        sendTextToServer(username);
//        new Message(MessageType.KEY, credentials.getPublicKey()).send(dataOutputStream);
//
//        // Receive server public key.
//        Message serverPublicKey = new Message(dataInputStream);
//        this.serverPublicKey = Key.class.cast(serverPublicKey.getBody());
//
//        // Receive encrypted Message.
//        System.out.println(new String(new DataCipher(EncryptionAlgorithm.RSA).decrypt(
//                new Message(dataInputStream).getBody().toString().getBytes(),
//                credentials.getPrivateKey())));

        // Inform the server of user disconnection.
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sendTextToServer("terminateme");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
    }

    public void sendTextToServer(final String message) {
        byte[] encodedBytes = MessageOperations.encode(MessageType.TEXT, message);
        new MessageOperations().send(encodedBytes, dataOutputStream);
    }

    private void messageReceived(final Message newMessage) throws IOException {
        this.display.newMessage(newMessage.getBody().toString());
    }

    private final class IncomingMessagesListener extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    messageReceived(new MessageOperations().read(dataInputStream));
                }
            } catch (Exception ex) {
            }
        }
    }
}







