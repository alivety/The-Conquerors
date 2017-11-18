package io.github.alivety.conquerors.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.PPLClient;
import io.github.alivety.ppl.SocketListener;
import io.github.alivety.ppl.packet.Packet;

public class TestAsClient extends Test {	
	public void test() {
		this.who="Client";
		final String host = JOptionPane.showInputDialog("Host");
		final int port = Integer.parseInt(JOptionPane.showInputDialog("Port"));
		Main.out.info(host + ":" + port);
		
		final PPLClient client = new PPLClient().addListener(new SocketListener() {
			public void connect(final SocketChannel ch) throws Exception {
				Main.out.info("connected");
				TestAsClient.this.SOCKET = ch;
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
							new PacketBuilder(TestAsClient.this, p, false).setVisible(true);
						} catch (final IllegalAccessException e) {
							Main.handleError(e);
						}
						;
					}
				});
				TestAsClient.this.pl.addRow(new Object[] { "Server", p.getId(), p });
			}
		});
		try {
			client.connect(host, port);
		} catch (final InterruptedException e) {
			Main.handleError(e);
		}
		
		TestAsClient.this.pl = new PacketList(this);
		TestAsClient.this.pl.setVisible(true);
		
		TestAsClient.this.plc = new PacketListChooser(this);
		TestAsClient.this.plc.setVisible(true);
	}
}
