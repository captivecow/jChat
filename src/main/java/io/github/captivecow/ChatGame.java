package io.github.captivecow;

import com.badlogic.gdx.Game;

public class ChatGame extends Game {

    private final ChatScreen chatScreen;
    private final JoinScreen joinScreen;
    private String username;

    public ChatGame() {
        chatScreen = new ChatScreen();
        joinScreen = new JoinScreen(this);
    }

    @Override
    public void create() {
        setScreen(joinScreen);
    }

    public ChatScreen getChatScreen() {
        return chatScreen;
    }

    @Override
    public void dispose(){
        System.out.println("Main game dispose");
        chatScreen.dispose();
        joinScreen.dispose();
    }
}
