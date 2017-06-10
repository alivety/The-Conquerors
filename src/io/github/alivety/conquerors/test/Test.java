package io.github.alivety.conquerors.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.SocketListener;

public class Test implements ConquerorsApp {
	public void go() {
		try {
			Main.setupLogger(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String host=JOptionPane.showInputDialog("Host");
		int port=Integer.parseInt(JOptionPane.showInputDialog("Port"));
		Main.out.info(host+":"+port);
		
		PPLClient client=new PPLClient().addListener(new SocketListener(){
			public void connect(SocketChannel ch) throws Exception {
				Main.out.info("connected");
				ch.write(Main.encode(Main.createPacket(0, "alivety",Main.PRO_VER)));
			}

			public void read(SocketChannel ch, ByteBuffer msg) throws Exception {
				Packet p=Main.decode(msg);
				Event evt=Main.resolver.resolve(p, null);
				Main.EVENT_BUS.bus(evt);
			}

			public void exception(SocketChannel h, Throwable t) {
				t.printStackTrace();
			}});
		try {
			client.connect(host, port);
		} catch (InterruptedException e) {
			Main.handleError(e);
		}
	}

	public PlayerObject[] getOnlinePlayers() {
		return new PlayerObject[]{};
	}

}
