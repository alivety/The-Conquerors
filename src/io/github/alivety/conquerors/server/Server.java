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

import javax.swing.JOptionPane;

import com.google.common.collect.Maps;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.DummyEvent;
import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketListener;

public class Server implements ConquerorsApp {
	HashMap<SocketChannel, PlayerObject> lookup = new HashMap<SocketChannel, PlayerObject>();
	private final List<PlayerObject> players = new ArrayList<PlayerObject>();
	private final HashMap<String, UnitObject> units = new HashMap<String, UnitObject>();
	private final Stack<Entry<PlayerObject, AbstractPacket>> packets = new Stack<Entry<PlayerObject, AbstractPacket>>();

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

				public void read(final SocketChannel ch, final ByteBuffer msg) throws Exception {
					final AbstractPacket p = Main.decode(msg);
					Server.this.push(Maps.immutableEntry(Server.this.lookup.get(ch), p));
				}

				public void exception(final SocketChannel h, final Throwable t) {
					Main.handleError(t);
				}
			});
			final int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number:"));
			server.bind(port);
			Main.out.info("started on port=" + port);
		} catch (final Exception e) {
			Main.handleError(e);
		}

		while (true)
			// long time=new Date().getTime();
			while (this.has()) {
				final Entry<PlayerObject, AbstractPacket> e = this.pop();
				try {
					final Event evt = Main.resolver.resolve(e.getValue(), e.getKey());
					Main.EVENT_BUS.bus(evt);
				} catch (final IllegalAccessException e1) {
					Main.handleError(e1);
				}
				Main.out.info(e.getKey() + ": " + e.getValue());
			}
	}

	protected void broadcast(final AbstractPacket p) {
		final Iterator<PlayerObject> iter = this.players.iterator();
		while (iter.hasNext()) {
			final PlayerObject pl = iter.next();
			if (pl.username() != null)
				pl.write(p);
		}
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

	public UnitObject unitBySpatialID(final String spatialID) {
		return this.units.get(spatialID);
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

	public void registerUnit(final Unit unit) {
		this.units.put(unit.getSpatialID(), unit);
	}

	public UnitObject unregister(final UnitObject u) {
		return this.units.remove(u);
	}

	private synchronized void push(final Entry<PlayerObject, AbstractPacket> e) {
		this.packets.push(e);
	}

	private synchronized Entry<PlayerObject, AbstractPacket> pop() {
		return this.packets.pop();
	}

	private synchronized boolean has() {
		return !this.packets.empty();
	}

	public PlayerObject[] getOnlinePlayers() {
		return players.toArray(new PlayerObject[players.size()]);
	}
}
