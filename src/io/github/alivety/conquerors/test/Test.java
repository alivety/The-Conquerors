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
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.SocketListener;

public class Test implements ConquerorsApp {
	public SocketChannel server;
	protected PacketList pl;
	private PacketListChooser plc;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final String host = JOptionPane.showInputDialog("Host");
			final int port = Integer.parseInt(JOptionPane.showInputDialog("Port"));
			Main.out.info(host + ":" + port);

			final PPLClient client = new PPLClient().addListener(new SocketListener() {
				public void connect(final SocketChannel ch) throws Exception {
					Main.out.info("connected");
					// ch.write(Main.encode(Main.createPacket(0,
					// "alivety",Main.PRO_VER)));
					Test.this.server = ch;
				}

				public void exception(final SocketChannel h, final Throwable t) {
					Main.handleError(t);
				}

				public void read(final SocketChannel ch, final ByteBuffer msg) throws Exception {
					final Packet p = Main.decode(msg);
					final JButton btn = new JButton("View Packet Data");
					btn.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent arg0) {
							try {
								new PacketBuilder(Test.this, p, false).setVisible(true);
							} catch (final IllegalAccessException e) {
								Main.handleError(e);
							}
							;
						}
					});
					Test.this.pl.addRow(new Object[] { "Server", p.getPacketID(), p });
				}
			});
			try {
				client.connect(host, port);
			} catch (final InterruptedException e) {
				Main.handleError(e);
			}

			this.pl = new PacketList(this);
			this.pl.setVisible(true);

			this.plc = new PacketListChooser(this);
			this.plc.setVisible(true);
		} catch (final Exception e) {
			Main.handleError(e);
		}
	}
}
