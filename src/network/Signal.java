package network;

enum Signal {

    USER_CONNECTED(0),
    USER_DISCONNECTED(1);

    private short value;

    Signal(final int value) {
        this.value = (short) value;
    }

    // Assuming Signal value will be always assigned in the same order of addition.
    public static Signal getSignal(final short value) {
        return Signal.values()[value];
    }

    public short getValue() {
        return value;
    }
}
