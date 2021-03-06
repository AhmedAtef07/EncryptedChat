package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MessageOperations {
    private static final int HEADER_SIZE = 6;

    /**
     * Message is composed of header and body.
     */
    public static byte[] encode(final MessageType messageType, final Object object) {
        byte[] data;

        switch (messageType) {
            case TEXT:
                data = String.class.cast(object).getBytes();
                break;
            case KEY:
                Key key = Key.class.cast(object);
                data = key.getEncoded();
                break;
            case BYTES:
                data = byte[].class.cast(object);
                break;
            default:
                throw new IllegalArgumentException("Invalid message type to encode with.");
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + HEADER_SIZE);
        byteBuffer.putInt(data.length + HEADER_SIZE); // Length
        byteBuffer.putShort(messageType.getValue()); // Message Type
        byteBuffer.put(data); // Content

        return byteBuffer.array();
    }

    public Message read(final DataInputStream dataInputStream) throws IOException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        int length = dataInputStream.readInt();
        MessageType messageType = MessageType.getMessageType(dataInputStream.readShort());

        byte[] contentBytes = new byte[length - 6];
        dataInputStream.readFully(contentBytes);

        switch (messageType) {
            case TEXT:
                String content = new String(contentBytes);
                return new Message(messageType, content);
            case KEY:
                DESKeySpec encodedKeySpec = null;
                try {
                    encodedKeySpec = new DESKeySpec(contentBytes);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                Key key = SecretKeyFactory.getInstance("DES").generateSecret(encodedKeySpec);
                return new Message(messageType, key);
            case BYTES:
                return new Message(messageType, contentBytes);
            default:
                throw new IllegalArgumentException("Invalid received message type.");
        }
    }

    public void send(final byte[] data, final DataOutputStream dataOutputStream)
            throws IOException {
        dataOutputStream.write(data, 0, data.length);
        dataOutputStream.flush();
    }
}
