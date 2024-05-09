package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager implements Runnable {
    private final Server server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String username;
    private Room currentRoom; // finish working on rooms

    public ClientManager(Socket socket, Server server) throws IOException {
        this.server = server;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            out.writeUTF("Enter your username:");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            username = in.readUTF();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.broadcast(username + " has joined the chat");
            while (true) {
                String message = null;
                try {
                    message = in.readUTF();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
                server.processCommand(message, this);
            }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}