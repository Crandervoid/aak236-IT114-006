package client;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSender implements Runnable {
    private final DataOutputStream out;

    public ClientSender(Socket socket) throws IOException {
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        // work on this
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}