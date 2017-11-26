package io.github.alivety.conquerors.test;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.PPLServer;
import io.github.alivety.ppl.SocketListener;
import io.github.alivety.ppl.packet.Packet;

public class TestAsServer extends Test {
	public void test() {
		this.who = "Server";
		final int port = Integer.parseInt(JOptionPane.showInputDialog("Port"));
		final PPLServer server = new PPLServer().addListener(new SocketListener() {
			@Override
			public void connect(final SocketChannel ch) throws Exception {
				Main.out.info("connected");
				TestAsServer.this.SOCKET = ch;
			}
			
			@Override
			public void exception(final SocketChannel h, final Throwable t) {
				Main.handleError(t);
			}
			
			@Override
			public void read(final SocketChannel ch, final ByteBuffer msg) throws Exception {
				final Packet p = Main.decode(msg);
				final JButton btn = new JButton("View Packet Data");
				btn.addActionListener(arg0 -> {
					try {
						new PacketBuilder(TestAsServer.this, p, false).setVisible(true);
					} catch (final IllegalAccessException e) {
						Main.handleError(e);
					}
					;
				});
				TestAsServer.this.pl.addRow(new Object[] { "Client - " + TestAsServer.this.SOCKET.getRemoteAddress(), p.getId(), p });
			}
		});
		TestAsServer.this.pl = new PacketList(this);
		TestAsServer.this.pl.setVisible(true);
		
		TestAsServer.this.plc = new PacketListChooser(this);
		TestAsServer.this.plc.setVisible(true);
		try {
			server.bind(port);
		} catch (final Exception e) {
			Main.handleError(e);
		}
	}
}
