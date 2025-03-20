package io.github.captivecow;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher
{
    public static void main( String[] args )
    {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);
        config.setForegroundFPS(60);
        config.setTitle("jChat");
        new Lwjgl3Application(new ChatGame(), config);
    }
}
