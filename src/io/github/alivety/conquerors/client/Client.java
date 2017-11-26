package io.github.alivety.conquerors.client;

import java.util.List;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;

import io.github.alivety.conquerors.client.events.ConnectEvent;
import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.SocketAdapter;
import io.github.alivety.ppl.packet.Packet;

public class Client extends PlayerObject implements ConquerorsApp {
	private final String hostport;
	public List<String> allies = new Vector<String>();
	
	public Client(final PPLAdapter adapter, final String hostport) {
		super(adapter);
		this.hostport = hostport;
	}
	
	private GameApp app;
	protected PPLAdapter server;
	
	@Override
	public PlayerObject[] getOnlinePlayers() {
		return null;
	}
	
	@Override
	public void go() {
		try {
			Main.setupLogger(this);
			final HostAndPort hap = HostAndPort.fromString(this.hostport);
			Main.EVENT_BUS.subscribe(new ClientEventSubscriber(Client.this));
			final PPLClient client = new PPLClient().addListener(new SocketAdapter() {
				@Override
				public void connect(final PPLAdapter adapter) throws Exception {
					Main.EVENT_BUS.bus(new ConnectEvent(adapter));
				}
				
				@Override
				public void read(final PPLAdapter adapter, final Packet packet) throws Exception {
					Main.out.debug("read: " + packet + " app?" + Client.this.app);
					final Event evt = Main.resolver.resolve(packet, null);
					while (Client.this.app == null)
						Thread.sleep(1);
					Client.this.app.scheduleTask(() -> {
						Main.out.debug("scheduled bus for " + evt);
						Main.EVENT_BUS.bus(evt);
					});
				}
				
				@Override
				public void exception(final PPLAdapter adapter, final Throwable t) {
					Main.handleError(t);
				}
			});
			client.connect(hap.getHost(), hap.getPortOrDefault(3033));
		} catch (final Exception e) {
			Main.handleError(e);
		} catch (final Error e) {
			Main.handleError(e);
		}
	}
	
	public boolean ready() {
		return this.app != null;
	}
	
	public GameApp getApp() {
		Preconditions.checkNotNull(this.app, "App has not been initialized");
		return this.app;
	}
	
	public void initApp() {
		Preconditions.checkArgument(this.app == null, "App has already initialized");
		Main.out.debug("initApp()");
		this.app = new GameApp(this);
	}
	
	public void ally(final String spatialID) {
		this.allies.add(spatialID);
	}
}
