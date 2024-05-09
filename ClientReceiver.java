package client;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver implements Runnable {
    private final Socket socket;
    private final ChatPopup chatPopup;

    public ClientReceiver(Socket socket, ChatPopup chatPopup) {
        this.socket = socket;
        this.chatPopup = chatPopup;
    }

    @Override
    public void run() {
        DataInputStream in = null;
        try {
            in = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            String message = null;
            try {
                message = in.readUTF();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            chatPopup.displayMessage(message);
        }
    }
}