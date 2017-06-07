package io.github.alivety.conquerors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Throwables;

import io.github.alivety.conquerors.Main;
import io.github.alivety.ppl.AbstractPacket;
import p.P10;
import p.P18;
import p.P8;

public class Player extends Unit {
	private final SocketChannel ch;
	private final Server server;
	private long created=new Date().getTime();
	protected String username=null;
	private int money,mpm=5;
	private AbstractPacket job;
	private boolean works=true;
	private List<String> unitsSpatialID=new ArrayList<String>();
	public Player(SocketChannel ch,Server server) {
		super(server);
		this.ch=ch;
		this.server=server;
	}
	
	private void write(ByteBuffer buf) throws IOException {
		ch.write(buf);
	}
	
	public void write(AbstractPacket p) {
		try {
			this.write(Main.encode(p));
		} catch (IOException e) {
			Main.handleError(e);
		}
	}
	
	public String username() {
		return username;
	}
	
	public AbstractPacket job() {
		return job;
	}

	@Override
	public String getUnitType() {
		return "player";
	}

	@Override
	public String getOwnerSpatialID() {
		return null;
	}
	
	public String toString() {
		return username()+" - "+spatialID;
	}
}
