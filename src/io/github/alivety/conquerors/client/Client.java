package io.github.alivety.conquerors.client;

import java.io.IOException;

import io.github.alivety.conquerors.ConquerorsApp;
import io.github.alivety.conquerors.Main;

public class Client implements ConquerorsApp {
	public void go() {
		try {
			Main.setupLogger(this);
		} catch (IOException e) {
			Main.handleError(e);
		}
	}
}
