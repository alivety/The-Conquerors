package io.github.alivety.conquerors.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;
import com.jme3.math.ColorRGBA;
import com.jme3.terrain.heightmap.HillHeightMap;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.Stack;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.window.Window;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketAdapter;
import io.github.alivety.ppl.packet.Packet;

public class Server implements ConquerorsApp {
	HashMap<PPLAdapter, PlayerObject> lookup = new HashMap<PPLAdapter, PlayerObject>();
	protected final List<PlayerObject> players = new ArrayList<PlayerObject>();
	protected final HashMap<String, Unit> units = new HashMap<String, Unit>();
	private final HashMap<String, Window> windows = new HashMap<>();
	private final Stack<Entry<PlayerObject, Packet>> packets = new Stack<Entry<PlayerObject, Packet>>();
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	public List<Integer> usedTeams = new Vector<>();
	private final int port;
	
	protected final ColorRGBA[] teams = { ColorRGBA.White, new ColorRGBA(102, 51, 153, 1), // purple
			ColorRGBA.Blue, ColorRGBA.Cyan, ColorRGBA.Gray, ColorRGBA.Green, ColorRGBA.Magenta, ColorRGBA.Pink, ColorRGBA.Red, ColorRGBA.Yellow };
	
	public boolean begin = false;
	
	public Server(final int port) throws Exception {
		this.port = port;
	}
	
	protected void broadcast(final Packet p) {
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject pl = iter.next();
			if (pl.username() != null)
				pl.write(p);
		}
	}
	
	@Override
	public PlayerObject[] getOnlinePlayers() {
		return this.players.toArray(new PlayerObject[] {});
	}
	
	@Override
	public void go() {
		try {
			Main.setupLogger(this);
		} catch (final IOException e2) {
			Main.handleError(e2);
		}
		Main.server = 1;
		Main.EVENT_BUS.subscribe(new ServerEventSubscriber(this));
		Main.PACKET_CATCHER.setVisible(true);
		try {
			final PPLServer server = new PPLServer().addListener(new SocketAdapter() {
				@Override
				public void connect(final PPLAdapter adapter) throws Exception {
					final PlayerObject player = new PlayerObject(adapter);
					Server.this.players.add(player);
					Server.this.lookup.put(adapter, player);
				}
				
				@Override
				public void read(final PPLAdapter adapter, final Packet packet) throws Exception {
					final PlayerObject player = Server.this.lookup.get(adapter);
					Main.out.debug("read: " + packet);
					Main.PACKET_CATCHER.addRow(new Object[] { player.username(), packet.getId(), packet });
					Server.this.packets_push(Maps.immutableEntry(Server.this.lookup.get(adapter), packet));
					Server.this.tasks_push(() -> {
						try {
							final Event evt = Main.resolver.resolve(packet, player);
							Main.EVENT_BUS.bus(evt);
						} catch (final Exception e) {
							Main.handleError(e);
						}
					});
				}
				
				@Override
				public void exception(final PPLAdapter adapter, final Throwable t) {
					final PlayerObject player = Server.this.lookup.get(adapter);
					Server.this.players.remove(player);
					
					if (t instanceof IOException) {
						Server.this.tasks_push(() -> Main.EVENT_BUS.bus(new PlayerDisconnectEvent(player)));
						return;
					}
					Main.handleError(t);
				}
			});
			server.bind(this.port);
			Main.out.info("started on port=" + this.port);
		} catch (final Exception e) {
			Main.handleError(e);
		}
		
		Main.ses.scheduleWithFixedDelay(() -> Server.this.tasks_push(() -> {
			for (final PlayerObject p : Server.this.getOnlinePlayers()) {
				p.money += p.mpm;
				p.write(Main.createPacket(19, p.money, p.mpm, p.getUnitSpatialIDs(), p.getAlliesSpatialID()));
			}
		}), 0, 1, TimeUnit.MINUTES);
		
		while (true) {
			while (this.tasks_has())
				this.tasks_pop().run();
			
			if ((this.getOnlinePlayers().length > 2) && (this.begin == false)) {
				this.begin = true;
				this.broadcast(Main.createPacket(9, Main.formatChatMessage("There are now enough players to begin the game")));
			}
		}
	}
	
	private synchronized void packets_push(final Entry<PlayerObject, Packet> e) {
		this.packets.push(e);
	}
	
	public PlayerObject playerByUsername(final String username) {
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject p = iter.next();
			if (p.username() != null)
				if (p.username().equals(username))
					return p;
		}
		return null;
	}
	
	public String[] playerList() {
		final List<String> names = new ArrayList<String>();
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject p = iter.next();
			if (p.username() != null)
				names.add(p.username());
		}
		return names.toArray(new String[names.size()]);
	}
	
	public void scheduleWindowOpen(final PlayerObject po, final Window w) {
		this.scheduleWindowOpen(Main.uuid(w.toString()), po, w);
	}
	
	public void scheduleWindowOpen(final String wID, final PlayerObject po, final Window w) {
		this.windows.put(wID, w);
		po.write(Main.createPacket(15, new Object[] { wID, w.getSlotValues() }));
	}
	
	public Window windowByID(final String wID) {
		return this.windows.get(wID);
	}
	
	public void registerUnit(final Unit unit) {
		this.units.put(unit.getSpatialID(), unit);
	}
	
	private synchronized boolean tasks_has() {
		return !this.tasks.empty();
	}
	
	private synchronized Runnable tasks_pop() {
		return this.tasks.pop();
	}
	
	private synchronized void tasks_push(final Runnable r) {
		this.tasks.push(r);
	}
	
	public UnitObject unitBySpatialID(final String spatialID) {
		return this.units.get(spatialID);
	}
	
	// private synchronized Entry<PlayerObject, Packet> packets_pop() {
	// return this.packets.pop();
	// }
	//
	// private synchronized boolean packets_has() {
	// return !this.packets.empty();
	// }
	
	public Unit unregister(final Unit u) {
		return this.units.remove(u.getSpatialID());
	}
}
