package client;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 404;

    public static void main(String[] args) throws IOException {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            ChatPopup chatPopup = new ChatPopup(socket, "YourUsername");
            chatPopup.initUI();
            new Thread(new ClientReceiver(socket, chatPopup)).start();
            new Thread(new ClientSender(socket)).start();
    }
}

