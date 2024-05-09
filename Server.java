package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    private final int port;
    private final List<Room> rooms;
    private final List<ClientManager> clients;

    public Server(int port) {
        this.port = port;
        this.rooms = new ArrayList<>();
        this.clients = new ArrayList<>();
    }
    public void start() throws IOException {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is now on port: " + port);
            new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String input = null;
                        try {
                            input = reader.readLine();
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        broadcast("Server: " + input);
                    }
            }).start();
            while (true) {
                Socket socket = serverSocket.accept();
                ClientManager clientHandler = new ClientManager(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
    }

    public void broadcast(String message) {
        for (ClientManager client : clients) {
            client.sendMessage(message);
        }
    }

    public void broadcastToRoom(String message, Room room) {
        for (ClientManager client : room.getClients()) {
            client.sendMessage(message);
        }
    }

    public void processJoinCommand(String message, ClientManager client) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
            String roomName = parts[1];
            Room room = findRoom(roomName);
            if (room == null) {
                room = new Room(roomName);
                rooms.add(room);
            }
            room.addClient(client);
            client.setCurrentRoom(room);
            broadcastToRoom(client.getUsername() + " has joined room " + roomName, room);
        }
        else {
            client.sendMessage("Invalid join command. Usage: /join [roomName]");
        }
    }
    public Room findRoom(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                return room;
            }
        }
        return null;
    }

    public void processCommand(String message, ClientManager client) {
        if (message.startsWith("/join")) {
            processJoinCommand(message, client);
        }
        else if (message.startsWith("/roll")) {
            rollDice(message);
        }
        else if (message.equals("/flip")) {
            flipCoin();
        }
        else {
            broadcast(client.getUsername() + ": " + message);
        }
    }

    private void rollDice(String message) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
                int max = Integer.parseInt(parts[1]);
                Random rand = new Random();
                int result = rand.nextInt(max) + 1;
                broadcast("Roll value: " + result);
        }
        else {
            broadcast("That is not a valid roll command");
        }
    }

    private void flipCoin() {
        Random rand = new Random();
        String value = rand.nextBoolean() ? "Heads" : "Tails";
            broadcast("Coin value: " + value);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(404);
        server.start();
    }
}