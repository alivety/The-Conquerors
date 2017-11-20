package io.github.alivety.conquerors.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

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
import io.github.alivety.ppl.SocketListener;
import io.github.alivety.ppl.packet.Packet;

public class Client extends PlayerObject implements ConquerorsApp {
	public Client(final PPLAdapter adapter) {
		super(adapter);
	}
	
	private GameApp app;
	protected PPLAdapter server;
	
	public PlayerObject[] getOnlinePlayers() {
		return null;
	}
	
	public void go() {
		try {
			Main.setupLogger(this);
			final String hostandport = JOptionPane.showInputDialog("host:port");
			final HostAndPort hap = HostAndPort.fromString(hostandport);
			Main.EVENT_BUS.subscribe(new ClientEventSubscriber(Client.this));
			PPLClient client=new PPLClient().addListener(new SocketAdapter(){
				@Override
				public void connect(PPLAdapter adapter) throws Exception {
					Main.EVENT_BUS.bus(new ConnectEvent(adapter));
				}

				@Override
				public void read(PPLAdapter adapter, Packet packet) throws Exception {
					final Event evt=Main.resolver.resolve(packet, null);
					Client.this.app.scheduleTask(new Runnable(){
						public void run() {
							Main.EVENT_BUS.bus(evt);
						}});
				}

				@Override
				public void exception(PPLAdapter adapter, Throwable t) {
					Main.handleError(t);
				}});
			client.connect(hap.getHost(), hap.getPortOrDefault(22));
		} catch (final Exception e) {
			Main.handleError(e);
		} catch (final Error e) {
			Main.handleError(e);
		}
	}
	
	public GameApp getApp() {
		Preconditions.checkNotNull(this.app, "App has not been initialized");
		return this.app;
	}
	
	public void initApp() {
		Preconditions.checkArgument(this.app == null, "App has already initialized");
		this.app = new GameApp(this);
	}
}
