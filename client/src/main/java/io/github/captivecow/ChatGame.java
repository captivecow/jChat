package io.github.captivecow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import io.github.captivecow.shared.Users;

import java.util.Objects;

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

    public void addJoinedUser(String username){
        Table userTable = chatScreen.getUserTable();
        userTable.add(username);
        userTable.row();
    }

    public void removeUser(String username){
        Table userTable = chatScreen.getUserTable();
        Array<Actor> actors = userTable.getChildren();

        for(int i = 0; i<actors.size; i++){
            Label userLabel = (Label) actors.get(i);
            if(Objects.equals(username, userLabel.getText().toString())){
                System.out.println("found " + username);
                userTable.removeActorAt(i, false);
                break;
            }
        }
    }
}
