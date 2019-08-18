package com.gamesbykevin.slide.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gamesbykevin.slide.MyGdxGame;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_HEIGHT;
import static com.gamesbykevin.slide.screen.ParentScreen.SCREEN_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SCREEN_WIDTH;
		config.height = SCREEN_HEIGHT;
		config.forceExit = true;

		//create a new game with our specified client
		MyGdxGame game = new MyGdxGame(new NoGameServiceClient());

		//create our game
		new LwjglApplication(game, config);
	}
}