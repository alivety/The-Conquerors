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
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.SocketListener;

public class Client extends PlayerObject implements ConquerorsApp {
	public Client(SocketChannel ch) {
		super(ch);
	}

	private GameApp app;
	protected SocketChannel server;

	public PlayerObject[] getOnlinePlayers() {
		return null;
	}

	public void go() {
		try {
			Main.setupLogger(this);
			final String hostandport = JOptionPane.showInputDialog("host:port");
			final HostAndPort hap = HostAndPort.fromString(hostandport);
			Main.EVENT_BUS.subscribe(new ClientEventSubscriber(Client.this));
			final PPLClient client = new PPLClient().addListener(new SocketListener() {
				public void connect(final SocketChannel ch) throws Exception {
					Main.EVENT_BUS.bus(new ConnectEvent(ch));
				}

				public void read(final SocketChannel ch, final ByteBuffer msg) throws Exception {
					final Event evt = Main.resolver.resolve(Main.decode(msg), null);
					Client.this.app.scheduleTask(new Runnable() {
						public void run() {
							Main.EVENT_BUS.bus(evt);
						}
					});
				}

				public void exception(final SocketChannel h, final Throwable t) {
					Main.handleError(t);
				}
			});
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
