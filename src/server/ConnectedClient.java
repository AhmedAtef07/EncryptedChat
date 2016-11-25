package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import network.Message;
import network.MessageOperations;
import network.MessageType;

class ConnectedClient extends Thread {

    private final ChatServer chatServer;

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
//    private final Key publicKey;

    public ConnectedClient(final Socket clientSocket, final ChatServer chatServer)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.chatServer = chatServer;

        this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
        this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        System.out.println("New connected client.");
        start();

//        sendMessage("ASD");

        // This is the first thing agreed with the client to send.
        // Receive client username.
//        Message usernameMessage = new Message(dataInputStream);
//        MessageOperations.encode(MessageType.TEXT, "Hey there");
//        this.username = usernameMessage.getBody().toString();
//
//        // This is the second thing agreed with the client to send.
//        // Receive the public key.
//        Message publicKeyMessage = new Message(dataInputStream);
//        this.publicKey = Key.class.cast(publicKeyMessage.getBody());
//
//
//        // Send the server public key to the client.
//        new Message(MessageType.KEY, chatServer.getCredentials().getPublicKey())
//                .send(dataOutputStream);

        // Send an encrypted test message to the client.
        // sendMessage(new String(
        //        new DataCipher(EncryptionAlgorithm.RSA).encrypt("Data!!".getBytes(), publicKey)));

        // Listen to any incoming messages on an external
    }

    public void sendMessage(final byte[] data) throws IOException {
        byte[] encodedBytes = MessageOperations.encode(MessageType.BYTES, data);
        new MessageOperations().send(encodedBytes, dataOutputStream);
    }

    private void receiveMessage(final Message newMessage) throws IOException {
        chatServer.broadcast((byte[]) newMessage.getBody());
    }

    @Override
    public void run() {
        try {
            while (true) {
                receiveMessage(new MessageOperations().read(dataInputStream));
            }
        } catch (Exception ex) {
        }
    }
}
