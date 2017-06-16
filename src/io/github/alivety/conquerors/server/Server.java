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

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.test.events.DummyEvent;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.SocketListener;

public class Server implements ConquerorsApp {
	HashMap<SocketChannel, PlayerObject> lookup = new HashMap<SocketChannel, PlayerObject>();
	private final List<PlayerObject> players = new ArrayList<PlayerObject>();
	private final HashMap<String, UnitObject> units = new HashMap<String, UnitObject>();
	private final Stack<Entry<PlayerObject, Packet>> packets = new Stack<Entry<PlayerObject, Packet>>();
	private final Stack<Runnable> tasks = new Stack<Runnable>();
	
	protected void broadcast(final Packet p) {
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject pl = iter.next();
			if (pl.username() != null)
				pl.write(p);
		}
	}
	
	public PlayerObject[] getOnlinePlayers() {
		return this.players.toArray(new PlayerObject[this.players.size()]);
	}
	
	public void go() {
		try {
			Main.setupLogger(this);
		} catch (final IOException e2) {
			Main.handleError(e2);
		}
		Main.server = 1;
		Main.EVENT_BUS.subscribe(new ServerEventSubscriber(this));
		Main.EVENT_BUS.bus(new DummyEvent());
		try {
			final PPLServer server = new PPLServer().addListener(new SocketListener() {
				public void connect(final SocketChannel ch) throws Exception {
					final PlayerObject p = new PlayerObject(ch);
					Server.this.players.add(p);
					Server.this.lookup.put(ch, p);
				}
				
				public void exception(final SocketChannel h, final Throwable t) {
					final PlayerObject p = Server.this.lookup.get(h);
					Server.this.players.remove(p);
					
					if (t instanceof IOException) {
						Server.this.tasks_push(new Runnable() {
							public void run() {
								Main.EVENT_BUS.bus(new PlayerDisconnectEvent(Server.this.lookup.get(h)));
							}
						});
						return;
					}
					Main.handleError(t);
				}
				
				public void read(final SocketChannel ch, final ByteBuffer msg) throws Exception {
					final Packet p = Main.decode(msg);
					Server.this.packets_push(Maps.immutableEntry(Server.this.lookup.get(ch), p));
					Server.this.tasks_push(new Runnable() {
						public void run() {
							try {
								final Event evt = Main.resolver.resolve(p, Server.this.lookup.get(ch));
								Main.EVENT_BUS.bus(evt);
							} catch (final Exception e) {
								Main.handleError(e);
							}
						}
					});
				}
			});
			final int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number:"));
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
							Main.out.debug(p + " money raised to " + p.money);
							p.write(Main.createPacket(19, p.money, p.mpm, p.getUnitSpatialIDs()));
						}
					}
				});
			}
		}, 0, 1, TimeUnit.MINUTES);
		
		while (true)
			while (this.tasks_has())
				this.tasks_pop().run();
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
