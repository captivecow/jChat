package io.github.captivecow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class JoinScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Table layout;
    private Label usernameLabel;
    private TextField usernameInput;
    private TextButton connect;
    private final ChatGame game;

    public JoinScreen(ChatGame game){
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        layout = new Table(skin);
        layout.setFillParent(true);

        usernameLabel = new Label("Username:", skin);
        usernameInput = new TextField("", skin);
        connect = new TextButton("Connect", skin);
        connect.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.getChatScreen().setUsername(usernameInput.getText());
                game.setScreen(game.getChatScreen());
            }
        });

        layout.add(usernameLabel);
        layout.add(usernameInput);
        layout.row();
        layout.add(connect).colspan(2).align(Align.center);

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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        System.out.println("Join screen hide");
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        System.out.println("Join screen dispose");
        stage.dispose();
        skin.dispose();
    }
}
