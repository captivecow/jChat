package io.github.captivecow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Objects;

public class ChatScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Table layout;
    private TextField inputChatText;
    private Table chatTable;
    private ScrollPane chatTablePane;
    private Table userTable;
    private ScrollPane userTablePane;
    private Label usernameLabel;
    private Table bottomLayout;
    private TextButton enterButton;
    private String username;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        inputChatText = new TextField("", skin);

        layout = new Table(skin);

        layout.setFillParent(true);

        chatTable = new Table(skin);
        chatTable.align(Align.left);
        chatTable.bottom();
        chatTablePane = new ScrollPane(chatTable, skin);

        userTable = new Table(skin);
        userTable.top();
        userTablePane = new ScrollPane(userTable, skin);

        bottomLayout = new Table();
        usernameLabel = new Label(username + ": ", skin);
        enterButton = new TextButton("Enter", skin);
        bottomLayout.add(usernameLabel);
        bottomLayout.add(inputChatText).expandX().fill();
        bottomLayout.add(enterButton);

        layout.add(chatTablePane).expand().fill().pad(0.0f, 0.0f, 0.0f, 5.0f)
                .prefWidth(stage.getViewport().getScreenWidth() * .7f);
        layout.add(userTablePane).expand().fill().pad(0.0f, 0.0f, 0.0f, 0.0f)
                .prefWidth(stage.getViewport().getScreenWidth() * .1f);

        layout.row();
        layout.add(bottomLayout).colspan(2).expandX().fill().pad(10.0f);
        layout.bottom();

        stage.addActor(layout);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (Objects.nonNull(stage)) {
            stage.dispose();
        }
        if (Objects.nonNull(skin)) {
            skin.dispose();
        }
    }

    public Table getUserTable(){
        return userTable;
    }
}
