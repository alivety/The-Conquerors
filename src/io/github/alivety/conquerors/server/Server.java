package io.github.alivety.conquerors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import javax.swing.JOptionPane;

import com.google.common.collect.Maps;

import io.github.alivety.conquerors.ConquerorsApp;
import io.github.alivety.conquerors.Main;
import io.github.alivety.conquerors.event.Event;
import io.github.alivety.conquerors.event.SubscribeEvent;
import io.github.alivety.conquerors.events.DummyEvent;
import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketListener;
import p.P0;

public class Server implements ConquerorsApp {
	HashMap<SocketChannel,Player> lookup=new HashMap<SocketChannel,Player>();
	private List<Player> players=new ArrayList<Player>();
	private HashMap<String,Unit> units=new HashMap<String,Unit>();
	private Stack<Entry<Player,AbstractPacket>> packets=new Stack<Entry<Player, AbstractPacket>>();
	public void go() {
		try {
			Main.setupLogger(this);
		} catch (IOException e2) {
			Main.handleError(e2);
		}
		Main.server=1;
		Main.EVENT_BUS.subscribe(new ServerEventSubscriber(this));
		Main.EVENT_BUS.bus(new DummyEvent());
		try {
			PPLServer server=new PPLServer().addListener(new SocketListener(){
				public void connect(SocketChannel ch) throws Exception {
					Player p=new Player(ch,Server.this);
					players.add(p);
					lookup.put(ch, p);
				}

				public void read(SocketChannel ch, ByteBuffer msg) throws Exception {
					AbstractPacket p=Main.decode(msg);
					Server.this.push(Maps.immutableEntry(lookup.get(ch), p));
				}

				public void exception(SocketChannel h, Throwable t) {
					Main.handleError(t);
				}});
			int port=Integer.parseInt(JOptionPane.showInputDialog("Enter port number:"));
			server.bind(port);
			Main.out.info("started on port="+port);
		} catch (Exception e) {
			Main.handleError(e);
		}
		
		while (true) {
			//long time=new Date().getTime();
			while (has()) {
				Entry<Player,AbstractPacket> e=pop();
				try {
					Event evt=Main.resolver.resolve(e.getValue(), e.getKey());
					Main.EVENT_BUS.bus(evt);
				} catch (IllegalAccessException e1) {
					Main.handleError(e1);
				}
				Main.out.info(e.getKey()+": "+e.getValue());
			}
		}
	}
	
	protected void broadcast(AbstractPacket p) {
		Iterator<Player> iter=players.iterator();
		while (iter.hasNext()) {
			Player pl=iter.next();
			if (pl.username()!=null) pl.write(p);
		}
	}
	
	/*@SubscribeEvent
	public void registerPlayer(Player p) {
		P0 p0=(P0) p.job();
		if (p0.protocolVersion!=Main.PRO_VER) p.write(Main.createPacket(2, "You are running a different version of the game"));
	    if (Arrays.asList(this.playerList()).contains(p0.username)) p.write(Main.createPacket(2, "Someone with that username is already connected"));
	    /*
	    p.username=p0.username;
	    p.write(Main.createPacket(1, p.spatialID));
	    broadcast(Main.createPacket(4, "model","material",p.spatialID));
	    broadcast(Main.createPacket(12, new Object[]{this.playerList()}));
	    //p.write(Main.createPacket(5,p.spatialID,x,y,z));
	    broadcast(Main.createPacket(9, Main.formatChatMessage(p.username+" has joined the game")));
	}*/
	
	protected void disconnect(Player p) {
		broadcast(Main.createPacket(11, p.spatialID));
		broadcast(Main.createPacket(9, Main.formatChatMessage(p.username+" has left the game")));
		p.username=null;
	}
	
	public String[] playerList() {
		List<String> names=new ArrayList<String>();
		Iterator<Player> iter=players.iterator();
		while (iter.hasNext()) {
			Player p=iter.next();
			if (p.username()!=null) {
				names.add(p.username());
			}
		}
		return names.toArray(new String[names.size()]);
	}
	
	public Unit unitBySpatialID(String spatialID) {
		return units.get(spatialID);
	}
	
	public Player playerByUsername(String username) {	
		Iterator<Player> iter=players.iterator();
		while (iter.hasNext()) {
			Player p=iter.next();
			if (p.username()!=null) {
				if (p.username.equals(username)) return p;
			}
		}
		return null;
	}
	
	public void registerUnit(Unit unit) {
		units.put(unit.getSpatialID(), unit);
	}
	
	public Unit unregister(Unit unit) {
		return units.remove(unit);
	}
	
	private synchronized void push(Entry<Player,AbstractPacket> e) {
		packets.push(e);
	}
	
	private synchronized Entry<Player,AbstractPacket> pop() {
		return packets.pop();
	}
	
	private synchronized boolean has() {
		return !packets.empty();
	}
}
