package user_interface;

public class CommandLineDisplay implements ClientDisplay {

    @Override
    public void newMessage(final String message) {
        System.out.println(message);
    }
}
