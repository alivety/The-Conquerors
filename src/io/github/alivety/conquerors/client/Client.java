package io.github.alivety.conquerors.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import com.google.common.net.HostAndPort;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
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
				}

				public void read(SocketChannel ch, ByteBuffer msg) throws Exception {
					
				}

				public void exception(SocketChannel h, Throwable t) {
					Main.handleError(t);
				}});
			client.connect(hap.getHost(), hap.getPort());
		} catch (final Exception e) {
			Main.handleError(e);
		} catch (Error e) {
			Main.handleError(e);
		}
	}
}
