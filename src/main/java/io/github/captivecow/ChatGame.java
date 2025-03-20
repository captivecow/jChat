package io.github.captivecow;

import com.badlogic.gdx.Game;

public class ChatGame extends Game {

    ChatScreen screen;

    public ChatGame(){
        screen = new ChatScreen();
    }

    @Override
    public void create() {
        setScreen(screen);
    }
}
