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
	
	public void handle(AbstractPacket p) {
		if (!works) return;
		try {
			job=p;
			int id=job.getPacketID();
			if (id==0) {
				server.registerPlayer(this);
			} else if (id==8) {
				P8 p8=(P8)job;
				server.broadcast(Main.createPacket(9, Main.formatChatMessage(this.username, p8.message)));
			} else if (id==10) {
				P10 p10=(P10)job;
				server.broadcast(Main.createPacket(5, this.spatialID, p10.x, p10.y, p10.z));
				this.teleport(Main.nv(p10.x, p10.y, p10.z));
			} else if (id==13) {
				server.disconnect(this);
				works = false;
			} else if (id==14) {
				//TODO windows
			} else if (id==16) {
				//TODO windows
			} else if (id==18) {
				P18 p18=(P18)job;
				for (String s : p18.spatialID) {
					if (!unitsSpatialID.contains(s)) {
						write(Main.createPacket(9, Main.formatChatMessage("You attempted to control a unit that does not belong to you. SHAME!")));
						return;
					}
					//TODO movement planning
				}
			}
			else {
				throw new IllegalArgumentException("Unknown packet "+p);
			}
		} catch (Exception e) {
			works = false;
			Main.out.error(Throwables.getStackTraceAsString(e));
			server.disconnect(this);
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
