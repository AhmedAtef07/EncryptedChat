package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Message {

    private short length;
    private MessageType type;
    private Object body;
    private byte[] raw;

    public Message(final MessageType type, final Object content) {
        encode(type, content);
    }

    public Message(final byte[] raw) {
        this.raw = raw;
        decode(raw);
    }

    public Message(final DataInputStream dataInputStream) throws IOException {
        read(dataInputStream);
        encode(type, body);
    }

    private void encode(final MessageType messageType, final Object object) {
        short length = 4;
        byte[] data = null;

        if (messageType.getTypeClass().equals(String.class)) {
            byte[] stringData = String.class.cast(object).getBytes();
            length += stringData.length;
            data = stringData;
        }
        if (messageType.getTypeClass().equals(Signal.class)) {
            length += 2;
            Signal signal = Signal.class.cast(object);
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort(signal.getValue());
            data = byteBuffer.array();
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.putShort(length);
        byteBuffer.putShort(messageType.getValue());
        byteBuffer.put(data);

        raw = byteBuffer.array();
        this.length = length;
    }

    private void decode(final byte[] raw) {
        short length = ByteBuffer.wrap(raw, 0, 2).getShort();
        MessageType messageType = MessageType.getMessageType(ByteBuffer.wrap(raw, 2, 2).getShort());

        if (messageType.getTypeClass().equals(String.class)) {
            String content = new String(raw, 4, length - 4);
            setLocalVariables(length, messageType, content);
        }
        if (messageType.getTypeClass().equals(Signal.class)) {
            Signal signal = Signal.getSignal(ByteBuffer.wrap(raw, 4, 2).getShort());
            setLocalVariables(length, messageType, signal);
        }
    }

    private void read(final DataInputStream dataInputStream) throws IOException {
        byte[] msg = new byte[length];
        dataInputStream.readFully(msg);

        short length = dataInputStream.readShort();
        MessageType messageType = MessageType.getMessageType(dataInputStream.readShort());
        byte[] byteContent = new byte[length - 4];
        dataInputStream.readFully(byteContent);

        if (messageType.getTypeClass().equals(String.class)) {
            String content = new String(byteContent);
            setLocalVariables(length, messageType, content);
        }
        if (messageType.getTypeClass().equals(Signal.class)) {
            Signal signal = Signal.getSignal(ByteBuffer.wrap(byteContent).getShort());
            setLocalVariables(length, messageType, signal);
        }
    }

    private void setLocalVariables(final short length, final MessageType type, final Object content) {
        this.length = length;
        this.type = type;
        this.body = content;
    }

    public short getLength() {
        return length;
    }

    public MessageType getType() {
        return type;
    }

    public Object getBody() {
        return body;
    }

    public byte[] getRaw() {
        return raw;
    }

    public boolean send(final DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.write(raw, 0, length);
            dataOutputStream.flush();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
