package io.github.alivety.conquerors.server;

import io.github.alivety.conquerors.ConquerorsApp;
import io.github.alivety.conquerors.Main;

public class Server implements ConquerorsApp {
	public void go() {
		Main.setupLogger(this);
		Main.out.info("server called upon");
	}
}
