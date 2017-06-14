package io.github.alivety.conquerors.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import com.google.common.net.HostAndPort;

import io.github.alivety.conquerors.client.events.ConnectEvent;
import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.SocketListener;

public class Client implements ConquerorsApp {
	private GameApp app;
	protected SocketChannel server;
	public PlayerObject[] getOnlinePlayers() {
		return null;
	}

	public void go() {
		try {
			Main.setupLogger(this);
			String hostandport=JOptionPane.showInputDialog("host:port");
			HostAndPort hap=HostAndPort.fromString(hostandport);
			PPLClient client=new PPLClient().addListener(new SocketListener(){
				public void connect(SocketChannel ch) throws Exception {
					Client.this.server=ch;
					Client.this.app=new GameApp(Client.this);
					Main.EVENT_BUS.subscribe(new ClientEventSubscriber(Client.this));
					Main.EVENT_BUS.bus(new ConnectEvent());
					app.start();
				}

				public void read(SocketChannel ch, ByteBuffer msg) throws Exception {
					final Event evt=Main.resolver.resolve(Main.decode(msg), null);
					app.scheduleTask(new Runnable(){
						public void run() {
							Main.EVENT_BUS.bus(evt);
						}});
				}

				public void exception(SocketChannel h, Throwable t) {
					Main.handleError(t);
				}});
			client.connect(hap.getHost(), hap.getPortOrDefault(22));
		} catch (final Exception e) {
			Main.handleError(e);
		} catch (Error e) {
			Main.handleError(e);
		}
	}
}
