package io.github.captivecow;

public class ServerLauncher {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
