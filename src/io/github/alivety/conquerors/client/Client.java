package io.github.alivety.conquerors.client;

import io.github.alivety.conquerors.ConquerorsApp;
import io.github.alivety.conquerors.Main;

public class Client implements ConquerorsApp {
	public void go() {
		Main.setupLogger(this);
	}
}
