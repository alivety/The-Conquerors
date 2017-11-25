package io.github.alivety.conquerors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.google.common.collect.Maps;
import com.jme3.math.ColorRGBA;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.window.Window;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.test.events.DummyEvent;
import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketAdapter;
import io.github.alivety.ppl.SocketListener;
import io.github.alivety.ppl.packet.Packet;

public class Server implements ConquerorsApp {
	HashMap<String, PlayerObject> lookup = new HashMap<String, PlayerObject>();
	protected final List<PlayerObject> players = new ArrayList<PlayerObject>();
	private final HashMap<String, UnitObject> units = new HashMap<String, UnitObject>();
	private final HashMap<String,Window> windows=new HashMap<>();
	private final Stack<Entry<PlayerObject, Packet>> packets = new Stack<Entry<PlayerObject, Packet>>();
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	private int port;
	
	protected final ColorRGBA[] teams={
			ColorRGBA.White,
			ColorRGBA.Black,
			ColorRGBA.Blue,
			ColorRGBA.Cyan,
			ColorRGBA.Gray,
			ColorRGBA.Green,
			ColorRGBA.Magenta,
			ColorRGBA.Pink,
			ColorRGBA.Red,
			ColorRGBA.Yellow
	};
	
	public boolean begin=false;
	
	public Server(int port) {
		this.port=port;
	}
	
	protected void broadcast(final Packet p) {
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject pl = iter.next();
			if (pl.username() != null)
				pl.write(p);
		}
	}
	
	public PlayerObject[] getOnlinePlayers() {
		return this.players.toArray(new PlayerObject[]{});
	}
	
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
			PPLServer server=new PPLServer().addListener(new SocketAdapter(){
				@Override
				public void connect(PPLAdapter adapter) throws Exception {
					PlayerObject player=new PlayerObject(adapter);
					Server.this.players.add(player);
					Server.this.lookup.put(adapter.toString(), player);
				}

				@Override
				public void read(PPLAdapter adapter, final Packet packet) throws Exception {
					final PlayerObject player=Server.this.lookup.get(adapter.toString());
					Main.PACKET_CATCHER.addRow(new Object[] {player.username(),packet.getId(),packet});
					Server.this.packets_push(Maps.immutableEntry(Server.this.lookup.get(adapter.toString()), packet));
					Server.this.tasks_push(new Runnable(){
						public void run() {
							try {
								Event evt=Main.resolver.resolve(packet,player);
								Main.EVENT_BUS.bus(evt);
							} catch (Exception e) {
								Main.handleError(e);
							}
						}});
				}

				@Override
				public void exception(PPLAdapter adapter, Throwable t) {
					final PlayerObject player = Server.this.lookup.get(adapter.toString());
					Server.this.players.remove(player);
					
					if (t instanceof IOException) {
						Server.this.tasks_push(new Runnable() {
							public void run() {
								Main.EVENT_BUS.bus(new PlayerDisconnectEvent(player));
							}
						});
						return;
					}
					Main.handleError(t);
				}});
			server.bind(port);
			Main.out.info("started on port=" + port);
		} catch (final Exception e) {
			Main.handleError(e);
		}
		
		Main.ses.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				Server.this.tasks_push(new Runnable() {
					public void run() {
						Main.out.debug("Updating player info");
						for (final PlayerObject p : Server.this.getOnlinePlayers()) {
							p.money += p.mpm;
							p.write(Main.createPacket(19, p.money, p.mpm, p.getUnitSpatialIDs()));
						}
					}
				});
			}
		}, 0, 1, TimeUnit.MINUTES);
		
		while (true) {
			while (this.tasks_has())
				this.tasks_pop().run();
			
			if (this.getOnlinePlayers().length>2 && begin==false) {
				begin=true;
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
	
	public void scheduleWindowOpen(PlayerObject po,Window w) {
		this.scheduleWindowOpen(Main.uuid(w.toString()), po, w);
	}
	
	public void scheduleWindowOpen(String wID,PlayerObject po,Window w) {
		windows.put(wID, w);
		po.write(Main.createPacket(15, new Object[] {wID, w.getSlotValues()}));
	}
	
	public Window windowByID(String wID) {
		return windows.get(wID);
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
	
	public UnitObject unregister(final UnitObject u) {
		return this.units.remove(u);
	}
}
