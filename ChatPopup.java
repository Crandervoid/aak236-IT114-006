package client;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class ChatPopup extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private Socket socket;
    private String username;

    public ChatPopup(Socket socket, String username) {
        this.socket = socket;
    }

    public void initUI() {
        setTitle("Chatroom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Chatroom");
        topPanel.add(titleLabel);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        JPanel bottomPanel = new JPanel();
        messageField = new JTextField(50);
        JButton sendButton = new JButton("SEND");
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        add(topPanel, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        sendButton.addActionListener(e -> {
            try {
                sendMessage(messageField.getText());
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        messageField.addActionListener(e -> {
            try {
                sendMessage(messageField.getText());
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    public void sendMessage(String message) throws IOException {
            ClientSender sender = new ClientSender(socket);
            sender.sendMessage(message);
    }
}