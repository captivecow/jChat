package io.github.captivecow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatScreen implements Screen {

    private Stage stage;
    private TextButton.TextButtonStyle buttonStyle;
    private Skin skin;
    private TextButton textButton;
    private Table table;
    private Label inputLabel;
    private TextField nameText;


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        buttonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButton = new TextButton("Click me", buttonStyle);
        inputLabel = new Label("Input:", skin);
        nameText = new TextField("", skin);

        table = new Table(skin);

        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        TextArea area = new TextArea("idk", skin);
        area.setDisabled(true);
        area.appendText("\n yoyo");

        Label someText = new Label("someText", skin);

        Table paneTable = new Table();
        ScrollPane pane = new ScrollPane(paneTable, skin);

        paneTable.align(Align.left);
        paneTable.add(someText);
        paneTable.bottom();

        table.add(pane).expand().fill().pad(10.0f);
        table.row();
        table.add(nameText).expandX().fillX().pad(10.0f);
        table.bottom();


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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
