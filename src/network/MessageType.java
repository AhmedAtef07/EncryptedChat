package network;

import java.security.Key;

public enum MessageType {

    TEXT(String.class),
    KEY(Key.class),
    BYTES(Byte[].class);

    private final Class typeClass;

    MessageType(final Class typeClass) {
        this.typeClass = typeClass;
    }

    // Assuming Type value will be always assigned in the same order of addition.
    public static MessageType getMessageType(final short value) {
        return MessageType.values()[value];
    }

    public short getValue() {
        return (short) this.ordinal();
    }
}
