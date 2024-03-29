package com.gamesbykevin.slide;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication {

	private GpgsClient client;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		//create our game services client
		this.client = new GpgsClient().initialize(this, false);

		//create a new game with our specified client
		MyGdxGame game = new MyGdxGame(this.client);

		//let's start
		initialize(game, config);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.client != null)
			this.client.onGpgsActivityResult(requestCode, resultCode, data);
	}
}