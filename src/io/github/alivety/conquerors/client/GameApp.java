package io.github.alivety.conquerors.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.Callable;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.CameraInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;

import io.github.alivety.conquerors.client.events.CreateModelEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;

public class GameApp extends SimpleApplication {
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private final Client client;
	
	private BulletAppState bullet=new BulletAppState();
	protected CharacterControl player;
	private RigidBodyControl entityBody;
	
	protected List<Spatial> selected=new Vector<>();
	
	private boolean showStats=false;
	
	private final Node entities = new Node("entities");
	
	private boolean dragToRotate = false;
	
	public final float SPEED=1f;
	
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
	
	public void removeSpatial(String spatialID) {
		this.entities.detachChildNamed(spatialID);
	}
	
	public void selectEntity(Spatial spat) {
		Main.out.debug(spat);
		if (selected.contains(spat)) {
			SceneGraphVisitorAdapter geometryGlowVisitor = new SceneGraphVisitorAdapter() {
			    @Override
			    public void visit(Geometry geom) {
			        Main.out.debug("visiting geomety " + geom.getName());
			        geom.getMaterial().clearParam("GlowColor");
			    }
			};
			spat.depthFirstTraversal(geometryGlowVisitor);
			selected.remove(spat);
		} else {
			SceneGraphVisitorAdapter geometryGlowVisitor = new SceneGraphVisitorAdapter() {
			    @Override
			    public void visit(Geometry geom) {
			        Main.out.debug("visiting geomety " + geom.getName());
			        geom.getMaterial().setColor("GlowColor", client.color);
			    }
			};
			spat.depthFirstTraversal(geometryGlowVisitor);
			selected.add(spat);
		}
	}
	
	@Override
	public void simpleInitApp() {
		Model.assetManager = this.assetManager;
		stateManager.attach(bullet);
		
		CapsuleCollisionShape capsule=new CapsuleCollisionShape(1.5f,6f,1);
		player=new CharacterControl(capsule,0.05f);
		player.setJumpSpeed(20);
	    player.setFallSpeed(30);
	    player.setGravity(30);
	    player.setPhysicsLocation(new Vector3f(0, 10, 0));
	    bullet.getPhysicsSpace().add(player);
		
		KeyEvents.app = this;
		this.initKeyBindings();
		
		this.setDisplayFps(showStats);
		this.setDisplayStatView(showStats);
		
		this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		final BitmapText ch = new BitmapText(this.guiFont, false);
		ch.setSize(this.guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation((this.settings.getWidth() / 2) - (ch.getLineWidth() / 2), (this.settings.getHeight() / 2) + (ch.getLineHeight() / 2), 0);
		this.guiNode.attachChild(ch);
		
		this.rootNode.attachChild(this.entities);
		
		
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		
	    this.enqueue(() -> {
			Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");

			Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
			grass.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex1", grass);
			mat_terrain.setFloat("Tex1Scale", 64f);

			Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
			dirt.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex2", dirt);
			mat_terrain.setFloat("Tex2Scale", 32f);

			Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
			rock.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex3", rock);
			mat_terrain.setFloat("Tex3Scale", 128f);
			
		    HillHeightMap heightmap = null;
		    HillHeightMap.NORMALIZE_RANGE = 100;
		    try {
		        heightmap = new HillHeightMap(513, 1000, 50, 100, (byte) 3);
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		    
		    mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));

		    int patchSize = 65;
		    TerrainQuad terrain = new TerrainQuad("the ground", patchSize, 513, heightmap.getHeightMap());

		    terrain.setMaterial(mat_terrain);
		    terrain.setLocalTranslation(0, -300, 0);
		    terrain.setLocalScale(2f, 1f, 2f);
		    entities.attachChild(terrain);

		    TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
		    terrain.addControl(control);
	    });
	    
	    FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
	    BloomFilter bloom=new BloomFilter(BloomFilter.GlowMode.Objects);
	    fpp.addFilter(bloom);
	    viewPort.addProcessor(fpp);
	}
	
	public void start() {
		super.start();
	}
	
	private boolean left,right,up,down;
	
	private void initKeyBindings() {
		this.inputManager.clearMappings();
		
		this.addKeyMapping(KeyInput.KEY_W, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				up=keyPressed;
			}});
		this.addKeyMapping(KeyInput.KEY_A, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				left=keyPressed;
			}});
		this.addKeyMapping(KeyInput.KEY_D, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				right=keyPressed;
			}});
		this.addKeyMapping(KeyInput.KEY_S, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				down=keyPressed;
			}});
		this.addKeyMapping(KeyInput.KEY_SPACE, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (keyPressed) player.jump();
			}});
		
		this.addKeyMapping(KeyInput.KEY_TAB, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (!keyPressed) {
					showStats=!showStats;
					setDisplayFps(showStats);
					setDisplayStatView(showStats);
				}
			}});
		
		this.addKeyMapping(KeyInput.KEY_ESCAPE, KeyEvents.ExitControl);
		this.addKeyMapping("Clear", KeyInput.KEY_C);
		inputManager.addListener(new ActionListener(){
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					Iterator<Spatial> iter=new Vector(selected).iterator();
					while (iter.hasNext()) {
						selectEntity(iter.next());
					}
				}
			}}, "Clear");
		this.addKeyMapping("Chat", KeyInput.KEY_T);//TODO open chat
		this.addKeyMapping("SelN", KeyInput.KEY_G);// TODO select nearby units
		this.addKeyMapping("Win", KeyInput.KEY_E);// TODO open window on selected unit
		
		inputManager.addMapping("select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (!keyPressed) {
					CollisionResults results = new CollisionResults();
					Ray ray = new Ray(cam.getLocation(), cam.getDirection());
					entities.collideWith(ray, results);
					if (results.size()>0) {
						Spatial spat=results.getClosestCollision().getGeometry();
						while (!spat.getParent().equals(entities)) {
							spat=spat.getParent();
						}
						if ("the ground".equals(spat.getName())) {
							List<String> id_list=new ArrayList<>();
							Iterator<Spatial> iter=selected.iterator();
							while (iter.hasNext()) {
								Spatial s=iter.next();
								if (client.username().equals(s.getUserData("owner"))) {
									id_list.add(s.getName());
								} else {
									Main.EVENT_BUS.bus(new PlayerChatEvent(null,Main.formatChatMessage("You cannot control units that are not yours")));
									return;
								}
								String[] spatialID=id_list.toArray(new String[] {});
								Vector3f loc=results.getClosestCollision().getContactPoint();
								try {
									client.server.writePacket(Main.createPacket(18, new Object[]{spatialID, (int)loc.x,(int)loc.y,(int)loc.z}));
								} catch (IOException e) {
									Main.handleError(e);
								}
							}
						} else {
							selectEntity(spat);
						}
					}
				}
			}}, "select");
		this.addKeyMapping(KeyInput.KEY_ADD, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				GameApp.this.scheduleTask(new Runnable(){
					@Override
					public void run() {
						if (!keyPressed) {
							String uuid=Main.uuid("testcube");
							Ray ray = new Ray(cam.getLocation(), cam.getDirection());
							CollisionResults results = new CollisionResults();
							entities.collideWith(ray, results);
							if (results.size() > 0) {
								Vector3f loc=results.getClosestCollision().getContactPoint();
						Main.EVENT_BUS.bus(new CreateModelEvent(uuid,
								new int[]{
										(int) loc.x,
										(int) loc.y,
										(int) loc.z
										},
								new int[][]{
							{0,255,255,255,0,0,0,0,1,1,1}
						}));
						Main.EVENT_BUS.bus(new EntityOwnershipChangedEvent(uuid, client.username()));
						}
						}
					}});
			}});
		
		this.addKeyMapping(KeyInput.KEY_J, new ActionListener(){
			@Override
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (!keyPressed) {
					player.setPhysicsLocation(new Vector3f(0,0,0));
				}
			}});
		
		this.addKeyMapping(KeyInput.KEY_P, new ActionListener() {
			public void onAction(final String name, final boolean keyPressed, final float tpf) {
				if (!keyPressed) {
					GameApp.this.dragToRotate = !GameApp.this.dragToRotate;
					GameApp.this.flyCam.setDragToRotate(GameApp.this.dragToRotate);
				}
			}
		});
		this.enqueue(() -> {
			inputManager.deleteMapping(CameraInput.FLYCAM_UP);
			inputManager.deleteMapping(CameraInput.FLYCAM_DOWN);
			inputManager.deleteMapping(CameraInput.FLYCAM_RIGHT);
			inputManager.deleteMapping(CameraInput.FLYCAM_LEFT);
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
						Main.out.debug(name + ": " + KeyEvents.getKey(name));
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
		
		CollisionShape shape=CollisionShapeFactory.createMeshShape(this.entities);
		bullet.getPhysicsSpace().remove(this.entityBody);
		this.entityBody=new RigidBodyControl(shape,0);
		bullet.getPhysicsSpace().add(this.entityBody);
		
		Vector3f camDir=cam.getDirection().multLocal(0.2f);
		Vector3f camLeft=cam.getLeft().multLocal(0.2f);
		Vector3f walkDirection=new Vector3f(0,0,0);
		if (left)
			walkDirection.addLocal(camLeft);
		if (right)
			walkDirection.addLocal(camLeft.negate());
		if (up)
			walkDirection.addLocal(camDir);
		if (down)
			walkDirection.addLocal(camDir.negate());
		
		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
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
