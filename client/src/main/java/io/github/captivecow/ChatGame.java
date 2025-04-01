package io.github.captivecow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.captivecow.shared.Users;

public class ChatGame extends Game {

    private final ChatScreen chatScreen;
    private final JoinScreen joinScreen;
    private ChatClient client;
    private String username;

    public ChatGame() {
        client = new ChatClient(this);
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
        client.disconnect();
        chatScreen.dispose();
        joinScreen.dispose();
    }

    public void addInitialUsers(Users users){
        setScreen(chatScreen);
        Table userTable = chatScreen.getUserTable();
        for(int i = 0; i<users.getUserCount(); i++){
            userTable.add(users.getUser(i));
            userTable.row();
        }
    }
}
