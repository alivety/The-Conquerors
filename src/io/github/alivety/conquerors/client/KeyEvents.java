package io.github.alivety.conquerors.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import io.github.alivety.conquerors.common.Main;

public class KeyEvents {
	public static GameApp app;
	
	public static final MovementControl MovementControl = new MovementControl();
	public static final ExitControl ExitControl = new ExitControl();
	
	private static class MovementControl implements AnalogListener {
		@Override
		public void onAnalog(final String name, final float value, final float tpf) {
			try {
				final String m = KeyEvents.getKey(name);
				
				Vector3f vel = new Vector3f();
				final Vector3f pos = KeyEvents.app.getCamera().getLocation().clone();
				final Camera cam = KeyEvents.app.getCamera();
				final CharacterControl player = KeyEvents.app.player;
				
				final Vector3f camDir = (cam.getDirection()).multLocal(0.06f);
				final Vector3f camLeft = (cam.getLeft()).multLocal(0.04f);
				final Vector3f walkDirection = new Vector3f(0, 0, 0);
				
				switch (m) {
					case "w":
						cam.getDirection(vel);
						vel.multLocal(value * KeyEvents.app.SPEED);
						pos.addLocal(vel);
						walkDirection.addLocal(camDir);
						break;
					case "s":
						cam.getDirection(vel);
						vel.multLocal(-value * KeyEvents.app.SPEED);
						pos.addLocal(vel);
						walkDirection.addLocal(camDir.negate());
						break;
					case "a":
						KeyEvents.app.getCamera().getLeft(vel);
						vel.multLocal(value * KeyEvents.app.SPEED);
						pos.addLocal(vel);
						walkDirection.addLocal(camLeft);
						break;
					case "d":
						KeyEvents.app.getCamera().getLeft(vel);
						vel.multLocal(-value * KeyEvents.app.SPEED);
						pos.addLocal(vel);
						walkDirection.addLocal(camLeft.negate());
						break;
					case "up":
						vel = new Vector3f(0, value * KeyEvents.app.SPEED, 0);
						pos.addLocal(vel);
						player.jump();
						
						final Matrix3f mat = new Matrix3f();
						mat.fromAngleNormalAxis(value, cam.getLeft());
						
						final Vector3f up = cam.getUp();
						final Vector3f left = cam.getLeft();
						final Vector3f dir = cam.getDirection();
						
						mat.mult(up, up);
						mat.mult(left, left);
						mat.mult(dir, dir);
						
						final Quaternion q = new Quaternion();
						q.fromAxes(left, up, dir);
						q.normalizeLocal();
						
						cam.setAxes(q);
						break;
					case "down":
						vel = new Vector3f(0, -value * KeyEvents.app.SPEED, 0);
						pos.addLocal(vel);
						// cam.setLocation(pos);
						
						final Matrix3f mat1 = new Matrix3f();
						mat1.fromAngleNormalAxis(-value, cam.getLeft());
						
						final Vector3f up1 = cam.getUp();
						final Vector3f left1 = cam.getLeft();
						final Vector3f dir1 = cam.getDirection();
						
						mat1.mult(up1, up1);
						mat1.mult(left1, left1);
						mat1.mult(dir1, dir1);
						
						final Quaternion q1 = new Quaternion();
						q1.fromAxes(left1, up1, dir1);
						q1.normalizeLocal();
						
						cam.setAxes(q1);
						break;
				}
				player.setWalkDirection(walkDirection);
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Main.handleError(e);
			}
		}
	}
	
	private static class ExitControl implements ActionListener {
		@Override
		public void onAction(final String name, final boolean keyPressed, final float tpf) {
			if (!keyPressed)
				try {
					KeyEvents.app.getClient().server.writePacket(Main.createPacket(13));
					KeyEvents.app.destroy();
					System.exit(0);
				} catch (final IOException e) {
					Main.handleError(e);
				}
		}
	}
	
	private static HashMap<Integer, String> keyCache = new HashMap<Integer, String>();
	
	public static String getKey(final int keyInput) throws IllegalArgumentException, IllegalAccessException {
		if (KeyEvents.keyCache.containsKey(keyInput))
			return KeyEvents.keyCache.get(keyInput);
		final Field[] fields = KeyInput.class.getDeclaredFields();
		for (final Field f : fields)
			if ((Integer) f.get(null) == keyInput)
				return f.getName().substring(4).toLowerCase();
		throw new IllegalArgumentException("No such key with value=" + keyInput);
	}
	
	public static String getKey(final String name) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
		return KeyEvents.getKey(Integer.parseInt(name.replace("key_map_", "")));
	}
}
