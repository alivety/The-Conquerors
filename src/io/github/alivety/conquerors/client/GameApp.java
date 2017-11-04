package io.github.alivety.conquerors.client;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import io.github.alivety.conquerors.common.Main;

public class GameApp extends SimpleApplication {
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private final Client client;
	
	private final Node entities = new Node("entities");
	
	private boolean dragToRotate = false;
	
	public GameApp(final Client client) {
		this.client = client;
	}
	
	public void scheduleAddEntity(final String spatialID, final String material, final String model) {
		this.scheduleTask(new Runnable() {
			public void run() {
				Spatial spat = null;
				try {
					spat = GameApp.newSpatial(GameApp.this.assetManager, model, material, spatialID);
				} catch (final Exception e) {
					Main.handleError(e);
				}
				spat.setLocalTranslation(1, -1, 1);
				GameApp.this.entities.attachChild(spat);
			}
		});
	}
	
	public void scheduleChangeEntityOwnership(final String spatialID, final String ownerSpatialID) {
		this.scheduleTask(new Runnable() {
			public void run() {
				final Spatial spat = GameApp.this.getSpatial(spatialID);
				spat.setUserData("owner", ownerSpatialID);
			}
		});
	}
	
	public Spatial getSpatial(final String spatialID) {
		final Spatial spat = this.entities.getChild(spatialID);
		if (spat == null)
			throw new NullPointerException("No entity with spatialID=" + spatialID);
		return spat;
	}
	
	@Override
	public void simpleInitApp() {
		Model.assetManager = this.assetManager;
		
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
		
		this.rootNode.attachChild(this.entities);
	}
	
	private void initKeyBindings() {
		this.inputManager.clearMappings();
		
		this.addKeyMapping(KeyInput.KEY_W, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_A, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_D, KeyEvents.MovementControl);
		this.addKeyMapping(KeyInput.KEY_S, KeyEvents.MovementControl);
		
		this.addKeyMapping(KeyInput.KEY_ESCAPE, KeyEvents.ExitControl);
		
		this.addKeyMapping("Clear", KeyInput.KEY_C);// TODO clear all selected
													// units
		this.addKeyMapping("Chat", KeyInput.KEY_SLASH);// TODO open chat
		this.addKeyMapping("SelN", KeyInput.KEY_G);// TODO select nearby units
		this.addKeyMapping("Win", KeyInput.KEY_E);// TODO open window on
													// selected unit
		
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
	
	private void addKeyMapping(final int key, final InputListener listener) {
		final String id = "key_map_" + key;
		this.inputManager.addMapping(id, new KeyTrigger(key));
		this.inputManager.addListener(listener, id);
		this.inputManager.addListener(new ActionListener() {
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (!keyPressed)
					try {
						Main.out.debug(name + ": " + KeyEvents.getKey(Integer.parseInt(name.replace("key_map_", ""))));
					} catch (final Exception e) {
						Main.handleError(e);
					}
			}
		}, id);
		Main.out.debug("Added input mapping for " + id);
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
	
	public static ColorRGBA lookupColor(final String color) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return (ColorRGBA) ColorRGBA.class.getDeclaredField(color).get(null);
	}
	
	private static Map<String, Class<?>> classmap = new HashMap<String, Class<?>>();
	
	public void addModel(final Model m) {
		this.entities.attachChild(m.build());
	}
	
	public static Spatial newSpatial(final AssetManager assetManager, final String model, final String material, final String spatialID) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		if (material.equals("conquerors_model")) {
			final String[] modeldata = model.split("_");
			final String modelname = modeldata[0];
			
			Class<?> modelclass = null;
			if (GameApp.classmap.containsKey(modelname))
				modelclass = GameApp.classmap.get(modelname);
			else {
				final List<Class<?>> classlist = Arrays.asList(Model.class.getDeclaredClasses());
				final Iterator<Class<?>> iter = classlist.iterator();
				while (iter.hasNext()) {
					final Class<?> cls = iter.next();
					if (cls.getSimpleName().toLowerCase().equals(modelname.toLowerCase()))
						modelclass = cls;
				}
			}
			if (modeldata.length == 1)
				return ((Model) modelclass.newInstance()).build();
			else
				return ((Model) modelclass.getConstructor(ColorRGBA.class).newInstance(GameApp.lookupColor(modeldata[1]))).build();
		}
		final Spatial spat = assetManager.loadModel(model);
		final Material mat = new Material(assetManager, material);
		spat.setMaterial(mat);
		spat.setName(spatialID);
		return spat;
	}
}
