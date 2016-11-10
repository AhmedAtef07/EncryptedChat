package network;

import java.security.Key;

public enum MessageType {

    SIGNAL(0, Signal.class),
    TEXT(1, String.class),
    KEY(2, Key.class);

    private final Class typeClass;
    private final short value;

    MessageType(final int value, final Class typeClass) {
        this.value = (short) value;
        this.typeClass = typeClass;
    }

    // Assuming Type value will be always assigned in the same order of addition.
    public static MessageType getMessageType(final short value) {
        return MessageType.values()[value];
    }

    public Class getTypeClass() {
        return typeClass;
    }

    public short getValue() {
        return value;
    }
}
