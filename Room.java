package server;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final List<ClientManager> clients;
    public Room(String name) {
        this.name = name;
        this.clients = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ClientManager> getClients() {
        return clients;
    }

    public void addClient(ClientManager client) {
        clients.add(client);
    }
}
