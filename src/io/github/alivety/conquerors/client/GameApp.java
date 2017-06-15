package io.github.alivety.conquerors.client;

import java.util.Stack;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import io.github.alivety.conquerors.common.Main;

public class GameApp extends SimpleApplication {
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private final Client client;

	private boolean dragToRotate = false;

	public GameApp(final Client client) {
		this.client = client;
	}

	@Override
	public void simpleInitApp() {
		KeyEvents.app = this;
		this.initKeyBindings();

		this.setDisplayFps(false);
		this.setDisplayStatView(false);

		this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		final BitmapText ch = new BitmapText(this.guiFont, false);
		ch.setSize(this.guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation((this.settings.getWidth() / 2) - (ch.getLineWidth() / 2), (this.settings.getHeight() / 2) + (ch.getLineHeight() / 2), 0);
		this.guiNode.attachChild(ch);
	}

	private void initKeyBindings() {
		this.inputManager.clearMappings();

		this.addKeyMapping("Up", KeyInput.KEY_W);
		this.addKeyMapping("Left", KeyInput.KEY_A);
		this.addKeyMapping("Right", KeyInput.KEY_D);
		this.addKeyMapping("Down", KeyInput.KEY_S);

		this.addKeyMapping("Exit", KeyInput.KEY_ESCAPE);

		this.addKeyMapping("Clear", KeyInput.KEY_C);// TODO clear all selected
													// units
		this.addKeyMapping("Chat", KeyInput.KEY_SLASH);// TODO open chat
		this.addKeyMapping("SelN", KeyInput.KEY_G);// TODO select nearby units
		this.addKeyMapping("Win", KeyInput.KEY_E);// TODO open window on
													// selected
													// unit

		this.addKeyMapping("Drag", KeyInput.KEY_P);

		this.inputManager.addListener(KeyEvents.MovementControl, "Up", "Left", "Right", "Down");
		this.inputManager.addListener(KeyEvents.ExitControl, "Exit");

		this.inputManager.addListener(new ActionListener() {
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (!keyPressed) {
					GameApp.this.dragToRotate = !GameApp.this.dragToRotate;
					GameApp.this.flyCam.setDragToRotate(GameApp.this.dragToRotate);
				}
			}
		}, "Drag");
	}

	private void addKeyMapping(final String name, final int key) {
		this.inputManager.addMapping(name, new KeyTrigger(key));
	}

	@Override
	public void simpleUpdate(final float tpf) {
		while (this.hasMoreTasks())
			this.getNextTask().run();
	}

	public Client getClient() {
		return this.client;
	}

	protected synchronized void scheduleTask(final Runnable r) {
		this.tasks.push(r);
	}

	private synchronized Runnable getNextTask() {
		return this.tasks.pop();
	}

	private synchronized boolean hasMoreTasks() {
		return !this.tasks.empty();
	}

	@Override
	public void handleError(final String errMsg, final Throwable t) {
		Main.handleError(new RuntimeException(errMsg, t));
	}
}
