package io.github.alivety.conquerors.client;

import java.io.IOException;

import com.jme3.input.controls.ActionListener;

import io.github.alivety.conquerors.common.Main;

public abstract class KeyEvents implements ActionListener {
	public static GameApp app;
	
	public static final MovementControl MovementControl=new MovementControl();
	public static final ExitControl ExitControl=new ExitControl();
	
	private static class MovementControl extends KeyEvents {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (!keyPressed) {
				//TODO movement
			}
		}
	}
	
	private static class ExitControl extends KeyEvents {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (!keyPressed) {
				try {
					app.getClient().server.write(Main.encode(Main.createPacket(13)));
					System.exit(0);
				} catch (IOException e) {
					Main.handleError(e);
				}
			}
		}
	}
}
