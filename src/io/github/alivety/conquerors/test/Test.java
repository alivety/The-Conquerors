package io.github.alivety.conquerors.test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
	public void go() {
		try {
			Main.setupLogger(this);
			Main.EVENT_BUS.subscribe(this);
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
				//ch.write(Main.encode(Main.createPacket(0, "alivety",Main.PRO_VER)));
				Test.this.server=ch;
			}

			public void read(SocketChannel ch, ByteBuffer msg) throws Exception {
				final Packet p=Main.decode(msg);
				JButton btn=new JButton("View Packet Data");
				btn.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						try {
							new PacketBuilder(Test.this,p,false).setVisible(true);
						}  catch (IllegalAccessException e) {
							Main.handleError(e);
						} ;
					}});
				Test.this.pl.addRow(new Object[]{"Server",p.getPacketID(),p});
			}

			public void exception(SocketChannel h, Throwable t) {
				Main.handleError(t);
			}});
		try {
			client.connect(host, port);
		} catch (InterruptedException e) {
			Main.handleError(e);
		}
		
		pl=new PacketList(this);
		pl.setVisible(true);
		
		plc=new PacketListChooser(this);
		plc.setVisible(true);
	}

	public PlayerObject[] getOnlinePlayers() {
		return new PlayerObject[]{};
	}

	@SubscribeEvent
	public void catchAll(Event evt) throws IllegalArgumentException, IllegalAccessException {
		
	}
}
