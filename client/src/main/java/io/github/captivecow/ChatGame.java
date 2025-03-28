package io.github.captivecow;

import com.badlogic.gdx.Game;

public class ChatGame extends Game {

    private final ChatScreen chatScreen;
    private final JoinScreen joinScreen;
    private ChatClient client;
    private String username;

    public ChatGame() {
        client = new ChatClient();
        chatScreen = new ChatScreen();
        joinScreen = new JoinScreen(this, client);
    }

    @Override
    public void create() {
        setScreen(joinScreen);
    }

    public ChatScreen getChatScreen() {
        return chatScreen;
    }

    @Override
    public void dispose() {
        chatScreen.dispose();
        joinScreen.dispose();
    }
}
