package io.github.alivety.conquerors.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.CameraInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import io.github.alivety.conquerors.client.events.CreateModelEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.Stack;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;
import io.github.alivety.conquerors.server.packets.PacketRequestWindow;

public class GameApp extends SimpleApplication {
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private final Client client;
	
	private final BulletAppState bullet = new BulletAppState();
	protected CharacterControl player;
	private RigidBodyControl entityBody;
	
	protected List<Spatial> selected = new Vector<>();
	
	private boolean showStats = false;
	
	private final Node entities = new Node("entities");
	
	private boolean dragToRotate = false;
	
	public final float SPEED = 0.4f;
	
	public GameApp(final Client client) {
		this.client = client;
	}
	
	public void scheduleAddEntity(final String spatialID, final String material, final String model) {
		this.scheduleTask(() -> {
			Spatial spat = null;
			try {
				spat = GameApp.newSpatial(GameApp.this.assetManager, model, material, spatialID);
			} catch (final Exception e) {
				Main.handleError(e);
			}
			spat.setLocalTranslation(1, -1, 1);
			GameApp.this.attachEntity(spat);
		});
	}
	
	public void scheduleChangeEntityOwnership(final String spatialID, final String ownerSpatialID) {
		this.scheduleTask(() -> {
			final Spatial spat = GameApp.this.getSpatial(spatialID);
			spat.setUserData("owner", ownerSpatialID);
		});
	}
	
	public Spatial getSpatial(final String spatialID) {
		final Spatial spat = this.entities.getChild(spatialID);
		if (spat == null)
			throw new NullPointerException("No entity with spatialID=" + spatialID);
		return spat;
	}
	
	public void removeSpatial(final String spatialID) {
		this.selected.remove(this.entities.getChild(spatialID));
		detachEntity(this.entities.getChild(spatialID));
	}
	
	public void selectEntity(final Spatial spat) {
		Main.out.debug(spat);
		if (this.selected.contains(spat)) {
			final SceneGraphVisitorAdapter geometryGlowVisitor = new SceneGraphVisitorAdapter() {
				@Override
				public void visit(final Geometry geom) {
					Main.out.debug("visiting geomety " + geom.getName());
					geom.getMaterial().clearParam("GlowColor");
				}
			};
			spat.depthFirstTraversal(geometryGlowVisitor);
			this.selected.remove(spat);
		} else {
			final SceneGraphVisitorAdapter geometryGlowVisitor = new SceneGraphVisitorAdapter() {
				@Override
				public void visit(final Geometry geom) {
					Main.out.debug("visiting geomety " + geom.getName());
					geom.getMaterial().setColor("GlowColor", GameApp.this.client.color);
				}
			};
			spat.depthFirstTraversal(geometryGlowVisitor);
			this.selected.add(spat);
		}
	}
	
	@Override
	public void simpleInitApp() {
		Model.assetManager = this.assetManager;
		this.stateManager.attach(this.bullet);
		
		final CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.5f, 6f, 1);
		this.player = new CharacterControl(capsule, 0.05f);
		this.player.setJumpSpeed(20);
		this.player.setFallSpeed(30);
		this.player.setGravity(30);
		this.player.setPhysicsLocation(new Vector3f(0, 10, 0));
		this.bullet.getPhysicsSpace().add(this.player);
		
		KeyEvents.app = this;
		this.initKeyBindings();
		
		this.setDisplayFps(this.showStats);
		this.setDisplayStatView(this.showStats);
		
		this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		final BitmapText ch = new BitmapText(this.guiFont, false);
		ch.setSize(this.guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation((this.settings.getWidth() / 2) - (ch.getLineWidth() / 2), (this.settings.getHeight() / 2) + (ch.getLineHeight() / 2), 0);
		this.guiNode.attachChild(ch);
		
		this.rootNode.attachChild(this.entities);
		
		this.viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		
		this.enqueue(() -> {
			final Material mat_terrain = new Material(this.assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
			
			final Texture grass = this.assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
			grass.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex1", grass);
			mat_terrain.setFloat("Tex1Scale", 64f);
			
			final Texture dirt = this.assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
			dirt.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex2", dirt);
			mat_terrain.setFloat("Tex2Scale", 32f);
			
			final Texture rock = this.assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
			rock.setWrap(WrapMode.Repeat);
			mat_terrain.setTexture("Tex3", rock);
			mat_terrain.setFloat("Tex3Scale", 128f);
			
			HillHeightMap heightmap = null;
			AbstractHeightMap.NORMALIZE_RANGE = 100;
			try {
				heightmap = new HillHeightMap(513, 1000, 50, 100, (byte) 3);
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
			
			mat_terrain.setTexture("Alpha", this.assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
			
			final int patchSize = 65;
			final TerrainQuad terrain = new TerrainQuad("the ground", patchSize, 513, heightmap.getHeightMap());
			
			terrain.setMaterial(mat_terrain);
			terrain.setLocalTranslation(0, -300, 0);
			terrain.setLocalScale(2f, 1f, 2f);
			this.attachEntity(terrain);
			
			final TerrainLodControl control = new TerrainLodControl(terrain, this.getCamera());
			terrain.addControl(control);
		});
		
		final FilterPostProcessor fpp = new FilterPostProcessor(this.assetManager);
		final BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bloom);
		this.viewPort.addProcessor(fpp);
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	private boolean left, right, up, down;
	
	private void initKeyBindings() {
		this.inputManager.clearMappings();
		
		this.addKeyMapping(KeyInput.KEY_W, (ActionListener) (name, keyPressed, tpf) -> GameApp.this.up = keyPressed);
		this.addKeyMapping(KeyInput.KEY_A, (ActionListener) (name, keyPressed, tpf) -> GameApp.this.left = keyPressed);
		this.addKeyMapping(KeyInput.KEY_D, (ActionListener) (name, keyPressed, tpf) -> GameApp.this.right = keyPressed);
		this.addKeyMapping(KeyInput.KEY_S, (ActionListener) (name, keyPressed, tpf) -> GameApp.this.down = keyPressed);
		this.addKeyMapping(KeyInput.KEY_SPACE, (ActionListener) (name, keyPressed, tpf) -> {
			if (keyPressed)
				GameApp.this.player.jump();
		});
		
		this.addKeyMapping(KeyInput.KEY_TAB, (ActionListener) (name, keyPressed, tpf) -> {
			if (!keyPressed) {
				GameApp.this.showStats = !GameApp.this.showStats;
				GameApp.this.setDisplayFps(GameApp.this.showStats);
				GameApp.this.setDisplayStatView(GameApp.this.showStats);
			}
		});
		
		this.addKeyMapping(KeyInput.KEY_ESCAPE, KeyEvents.ExitControl);
		this.addKeyMapping("Clear", KeyInput.KEY_C);
		this.inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
			if (!isPressed) {
				final Iterator<Spatial> iter = new Vector(GameApp.this.selected).iterator();
				while (iter.hasNext())
					GameApp.this.selectEntity(iter.next());
			}
		}, "Clear");
		this.addKeyMapping("Chat", KeyInput.KEY_T);// TODO open chat
		this.addKeyMapping("SelN", KeyInput.KEY_G);
		this.inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
			if (!isPressed) {
				final float radius = 24;
				final BoundingSphere s = new BoundingSphere(radius, GameApp.this.cam.getLocation().add(0, -6, 0));
				final CollisionResults results = new CollisionResults();
				GameApp.this.entities.collideWith(s, results);
				for (int i = 0; i < results.size(); i++) {
					Spatial spat = results.getCollision(i).getGeometry();
					if (spat == null)
						continue;
					while (!spat.getParent().equals(GameApp.this.entities))
						spat = spat.getParent();
					if (GameApp.this.client.username().equals(spat.getUserData("owner")))
						if (!GameApp.this.selected.contains(spat))
							GameApp.this.selectEntity(spat);
				}
			}
		}, "SelN");
		this.addKeyMapping("Win", KeyInput.KEY_E);
		this.inputManager.addListener((ActionListener) (name, isPressed, tpf) -> {
			if (!isPressed) {
				final PacketRequestWindow prw = new PacketRequestWindow();
				prw.spatialID = GameApp.this.selected.get(0).getName();
				try {
					GameApp.this.client.server.writePacket(prw);
				} catch (final IOException e) {
					Main.handleError(e);
				}
			}
		}, "Win");
		
		this.inputManager.addMapping("select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		this.inputManager.addListener((ActionListener) (name, keyPressed, tpf) -> {
			if (!keyPressed) {
				final CollisionResults results = new CollisionResults();
				final Ray ray = new Ray(GameApp.this.cam.getLocation(), GameApp.this.cam.getDirection());
				GameApp.this.entities.collideWith(ray, results);
				if (results.size() > 0) {
					Spatial spat = results.getClosestCollision().getGeometry();
					while (!spat.getParent().equals(GameApp.this.entities))
						spat = spat.getParent();
					if ("the ground".equals(spat.getName())) {
						final List<String> id_list = new ArrayList<>();
						final Iterator<Spatial> iter = GameApp.this.selected.iterator();
						while (iter.hasNext()) {
							final Spatial s = iter.next();
							if (GameApp.this.client.username().equals(s.getUserData("owner")))
								id_list.add(s.getName());
							else {
								Main.EVENT_BUS.bus(new PlayerChatEvent(null, Main.formatChatMessage("You cannot control units that are not yours")));
								return;
							}
						}
						final String[] spatialID = id_list.toArray(new String[] {});
						final Vector3f loc = results.getClosestCollision().getContactPoint();
						System.out.println(loc);
						try {
							GameApp.this.client.server.writePacket(Main.createPacket(18, new Object[] { spatialID, (int) loc.x, (int) loc.y, (int) loc.z }));
						} catch (final IOException e) {
							Main.handleError(e);
						}
					} else if (GameApp.this.client.username().equals(spat.getUserData("owner")) || GameApp.this.client.allies.contains(spat.getUserData("owner")))
						GameApp.this.selectEntity(spat);
				}
			}
		}, "select");
		this.addKeyMapping(KeyInput.KEY_ADD, (ActionListener) (name, keyPressed, tpf) -> GameApp.this.scheduleTask(() -> {
			if (!keyPressed) {
				final String uuid = Main.uuid("testcube");
				final Ray ray = new Ray(GameApp.this.cam.getLocation(), GameApp.this.cam.getDirection());
				final CollisionResults results = new CollisionResults();
				GameApp.this.entities.collideWith(ray, results);
				if (results.size() > 0) {
					final Vector3f loc = results.getClosestCollision().getContactPoint();
					Main.EVENT_BUS.bus(new CreateModelEvent(uuid, new float[] { loc.x, loc.y, loc.z }, new float[][] { { 0, 255, 255, 255, 0, 0, 0, 0, 1, 1, 1 } }));
					Main.EVENT_BUS.bus(new EntityOwnershipChangedEvent(uuid, GameApp.this.client.username()));
				}
			}
		}));
		
		this.addKeyMapping(KeyInput.KEY_J, (ActionListener) (name, keyPressed, tpf) -> {
			if (!keyPressed)
				GameApp.this.player.setPhysicsLocation(new Vector3f(0, 0, 0));
		});
		
		this.addKeyMapping(KeyInput.KEY_P, (ActionListener) (name, keyPressed, tpf) -> {
			if (!keyPressed) {
				GameApp.this.dragToRotate = !GameApp.this.dragToRotate;
				GameApp.this.flyCam.setDragToRotate(GameApp.this.dragToRotate);
			}
		});
		this.enqueue(() -> {
			this.inputManager.deleteMapping(CameraInput.FLYCAM_UP);
			this.inputManager.deleteMapping(CameraInput.FLYCAM_DOWN);
			this.inputManager.deleteMapping(CameraInput.FLYCAM_RIGHT);
			this.inputManager.deleteMapping(CameraInput.FLYCAM_LEFT);
		});
	}
	
	private void addKeyMapping(final String name, final int key) {
		this.inputManager.addMapping(name, new KeyTrigger(key));
	}
	
	private void addKeyMapping(final int key, final InputListener listener) {
		final String id = "key_map_" + key;
		this.inputManager.addMapping(id, new KeyTrigger(key));
		this.inputManager.addListener(listener, id);
		this.inputManager.addListener((ActionListener) (name, keyPressed, tpf) -> {
			if (!keyPressed)
				try {
					Main.out.debug(name + ": " + KeyEvents.getKey(name));
				} catch (final Exception e) {
					Main.handleError(e);
				}
		}, id);
		Main.out.debug("Added input mapping for " + id);
	}
	
	@Override
	public void simpleUpdate(final float tpf) {
		while (this.hasMoreTasks())
			this.getNextTask().run();
		
		/*final CollisionShape shape = CollisionShapeFactory.createMeshShape(this.entities);
		this.bullet.getPhysicsSpace().remove(this.entityBody);
		this.entityBody = new RigidBodyControl(shape, 0);
		this.bullet.getPhysicsSpace().add(this.entityBody);*/
		
		final Vector3f camDir = this.cam.getDirection().multLocal(this.speed);
		final Vector3f camLeft = this.cam.getLeft().multLocal(this.speed);
		final Vector3f walkDirection = new Vector3f(0, 0, 0);
		if (this.left)
			walkDirection.addLocal(camLeft);
		if (this.right)
			walkDirection.addLocal(camLeft.negate());
		if (this.up)
			walkDirection.addLocal(camDir);
		if (this.down)
			walkDirection.addLocal(camDir.negate());
		
		this.player.setWalkDirection(walkDirection);
		this.cam.setLocation(this.player.getPhysicsLocation());
		
		Iterator<Spatial> itr=entities.getChildren().iterator();
		while(itr.hasNext()) {
			Spatial spat=itr.next();
			spat.setLocalTranslation(spatControls.get(spat).getPhysicsLocation());
		}
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
		this.attachEntity(m.build());
	}
	
	private HashMap<Spatial,CharacterControl> spatControls=new HashMap<>();
	
	public void attachEntity(Spatial spat) {
		this.entities.attachChild(spat);
		CollisionShape shape = CollisionShapeFactory.createMeshShape(spat);
		CharacterControl cc=new CharacterControl(shape,0.05f);
		cc.setJumpSpeed(20);
		cc.setFallSpeed(30);
		cc.setGravity(30);
		cc.setPhysicsLocation(spat.getLocalTranslation());
		this.bullet.getPhysicsSpace().add(cc);
		spatControls.put(spat, cc);
	}
	
	public void detachEntity(Spatial spat) {
		this.entities.detachChild(spat);
		bullet.getPhysicsSpace().remove(spatControls.get(spat));
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

	public CharacterControl getSpatialControl(String spatialID) {
		return spatControls.get(entities.getChild(spatialID));
	}
}
