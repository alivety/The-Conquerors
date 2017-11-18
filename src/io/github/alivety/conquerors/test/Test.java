package io.github.alivety.conquerors.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.event.SubscribeEvent;
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.SocketListener;
import io.github.alivety.ppl.packet.Packet;

public class Test implements ConquerorsApp {
	public SocketChannel SOCKET;
	public PacketList pl;
	public PacketListChooser plc;
	public String who="?";
	@SubscribeEvent
	public void catchAll(final Event evt) throws IllegalArgumentException, IllegalAccessException {
		
	}
	
	public PlayerObject[] getOnlinePlayers() {
		return new PlayerObject[] {};
	}
	
	public void go() {
		
		try {
			try {
				Main.setupLogger(this);
				Main.EVENT_BUS.subscribe(this);
			} catch (final IOException e) {
				Main.handleError(e);
			}
			
			int o=JOptionPane.showOptionDialog(null, "Server/Client Test", "Test", 0, 0, null, new String[]{"Client","Server"}, "Client");
			if (o==0) {
				new TestAsClient().test();
			} else {
				new TestAsServer().test();
			}
		} catch (final Exception e) {
			Main.handleError(e);
		}
	}
}
