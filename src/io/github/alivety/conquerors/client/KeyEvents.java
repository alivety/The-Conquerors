package io.github.alivety.conquerors.client;

import java.io.IOException;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

import io.github.alivety.conquerors.common.Main;

public class KeyEvents {
	public static GameApp app;
	
	public static final MovementControl MovementControl=new MovementControl();
	public static final ExitControl ExitControl=new ExitControl();
	
	private static class MovementControl implements AnalogListener {
		public void onAnalog(String name, float value, float tpf) {
			//TODO mvement
		}
	}
	
	private static class ExitControl implements ActionListener {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (!keyPressed) {
				try {
					app.getClient().server.write(Main.encode(Main.createPacket(13)));
					app.destroy();
					System.exit(0);
				} catch (IOException e) {
					Main.handleError(e);
				}
			}
		}
	}
}
