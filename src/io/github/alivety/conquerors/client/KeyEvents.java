package io.github.alivety.conquerors.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

import io.github.alivety.conquerors.common.Main;

public class KeyEvents {
	public static GameApp app;

	public static final MovementControl MovementControl = new MovementControl();
	public static final ExitControl ExitControl = new ExitControl();

	private static class MovementControl implements AnalogListener {
		public void onAnalog(final String name, final float value, final float tpf) {
			// TODO mvement
		}
	}

	private static class ExitControl implements ActionListener {
		public void onAction(final String name, final boolean keyPressed, final float tpf) {
			if (!keyPressed)
				try {
					KeyEvents.app.getClient().server.write(Main.encode(Main.createPacket(13)));
					KeyEvents.app.destroy();
					System.exit(0);
				} catch (final IOException e) {
					Main.handleError(e);
				}
		}
	}
	
	private static HashMap<Integer,String> keyCache=new HashMap<Integer,String>();
	public static String getKey(int keyInput) throws IllegalArgumentException, IllegalAccessException {
		if (keyCache.containsKey(keyInput)) {
			return keyCache.get(keyInput);
		}
		Field[] fields=KeyInput.class.getDeclaredFields();
		for (Field f : fields) {
			if ((Integer)f.get(null)==keyInput) {
				return f.getName().substring(4).toLowerCase();
			}
		}
		throw new IllegalArgumentException("No such key with value="+keyInput);
	}
}
