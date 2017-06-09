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
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.DummyEvent;
import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketListener;

public class Server implements ConquerorsApp {
	HashMap<SocketChannel, Player> lookup = new HashMap<SocketChannel, Player>();
	private final List<Player> players = new ArrayList<Player>();
	private final HashMap<String, Unit> units = new HashMap<String, Unit>();
	private final Stack<Entry<Player, AbstractPacket>> packets = new Stack<Entry<Player, AbstractPacket>>();

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
					final Player p = new Player(ch, Server.this);
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
				final Entry<Player, AbstractPacket> e = this.pop();
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
		final Iterator<Player> iter = this.players.iterator();
		while (iter.hasNext()) {
			final Player pl = iter.next();
			if (pl.username() != null)
				pl.write(p);
		}
	}

	/*
	 * @SubscribeEvent public void registerPlayer(Player p) { P0 p0=(P0)
	 * p.job(); if (p0.protocolVersion!=Main.PRO_VER)
	 * p.write(Main.createPacket(2,
	 * "You are running a different version of the game")); if
	 * (Arrays.asList(this.playerList()).contains(p0.username))
	 * p.write(Main.createPacket(2,
	 * "Someone with that username is already connected")); /*
	 * p.username=p0.username; p.write(Main.createPacket(1, p.spatialID));
	 * broadcast(Main.createPacket(4, "model","material",p.spatialID));
	 * broadcast(Main.createPacket(12, new Object[]{this.playerList()}));
	 * //p.write(Main.createPacket(5,p.spatialID,x,y,z));
	 * broadcast(Main.createPacket(9,
	 * Main.formatChatMessage(p.username+" has joined the game"))); }
	 */

	protected void disconnect(final Player p) {
		this.broadcast(Main.createPacket(11, p.spatialID));
		this.broadcast(Main.createPacket(9, Main.formatChatMessage(p.username + " has left the game")));
		p.username = null;
	}

	public String[] playerList() {
		final List<String> names = new ArrayList<String>();
		final Iterator<Player> iter = this.players.iterator();
		while (iter.hasNext()) {
			final Player p = iter.next();
			if (p.username() != null)
				names.add(p.username());
		}
		return names.toArray(new String[names.size()]);
	}

	public Unit unitBySpatialID(final String spatialID) {
		return this.units.get(spatialID);
	}

	public Player playerByUsername(final String username) {
		final Iterator<Player> iter = this.players.iterator();
		while (iter.hasNext()) {
			final Player p = iter.next();
			if (p.username() != null)
				if (p.username.equals(username))
					return p;
		}
		return null;
	}

	public void registerUnit(final Unit unit) {
		this.units.put(unit.getSpatialID(), unit);
	}

	public Unit unregister(final Unit unit) {
		return this.units.remove(unit);
	}

	private synchronized void push(final Entry<Player, AbstractPacket> e) {
		this.packets.push(e);
	}

	private synchronized Entry<Player, AbstractPacket> pop() {
		return this.packets.pop();
	}

	private synchronized boolean has() {
		return !this.packets.empty();
	}
}
