package io.github.alivety.conquerors.client;

import java.io.IOException;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;

public class Client implements ConquerorsApp {
	public PlayerObject[] getOnlinePlayers() {
		return null;
	}

	public void go() {
		try {
			Main.setupLogger(this);
		} catch (final IOException e) {
			Main.handleError(e);
		}
	}
}
