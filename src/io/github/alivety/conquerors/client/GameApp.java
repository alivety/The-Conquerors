package io.github.alivety.conquerors.client;

import java.util.Stack;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import io.github.alivety.conquerors.common.Main;

public class GameApp extends SimpleApplication {
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private final Client client;
	
	private Node entities=new Node("entities");

	private boolean dragToRotate = false;

	public GameApp(final Client client) {
		this.client = client;
	}
	
	public void scheduleAddEntity(final String spatialID, final String material, final String model) {
		this.scheduleTask(new Runnable(){
			public void run() {
				Material mat=new Material(assetManager,material);
				Spatial spat=assetManager.loadModel(model);
				spat.setName(spatialID);
				spat.setMaterial(mat);
				entities.attachChild(spat);
			}});
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

		this.addKeyMapping(KeyInput.KEY_W, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_A, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_D, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_S, KeyEvents.MovementControl);

		this.addKeyMapping(KeyInput.KEY_ESCAPE, KeyEvents.ExitControl);

		this.addKeyMapping("Clear", KeyInput.KEY_C);// TODO clear all selected units
		this.addKeyMapping("Chat", KeyInput.KEY_SLASH);// TODO open chat
		this.addKeyMapping("SelN", KeyInput.KEY_G);// TODO select nearby units
		this.addKeyMapping("Win", KeyInput.KEY_E);// TODO open window on selected unit

		this.addKeyMapping(KeyInput.KEY_P, new ActionListener() {
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (!keyPressed) {
					GameApp.this.dragToRotate = !GameApp.this.dragToRotate;
					GameApp.this.flyCam.setDragToRotate(GameApp.this.dragToRotate);
				}
			}
		});
	}

	private void addKeyMapping(final String name, final int key) {
		this.inputManager.addMapping(name, new KeyTrigger(key));
	}
	
	private void addKeyMapping(int key,InputListener listener) {
		String id="key_map_"+key;
		inputManager.addMapping(id, new KeyTrigger(key));
		inputManager.addListener(listener, id);
		inputManager.addListener(new ActionListener(){
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (!keyPressed) {
					try {
						Main.out.debug(name+": "+KeyEvents.getKey(Integer.parseInt(name.replace("key_map_", ""))));
					} catch (Exception e) {
						Main.handleError(e);
					}
				}
			}}, id);
		Main.out.debug("Added input mapping for "+id);
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
