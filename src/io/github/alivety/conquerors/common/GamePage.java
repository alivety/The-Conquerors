package io.github.alivety.conquerors.common;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.test.ButtonColumn;
import io.github.alivety.conquerors.test.PacketBuilder;
import io.github.alivety.ppl.packet.Packet;

import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;

public class GamePage extends JFrame {
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private TableModel model;
	private JTextField textField_1;
	private JTextField textField_2;
	
	public GamePage(final Preferences.Conquerors conq, JSONArray servers) {
		setTitle("The Conquerors");
		setUndecorated(true);
		setResizable(false);
		setBackground(new Color(0, 102, 153));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 102, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnx = new JButton("<html><b>X");
		btnx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					conq.save();
				} catch (IOException e1) {
					Main.handleError(e1);
				}
				GamePage.this.setVisible(false);
				System.exit(0);
			}
		});
		btnx.setForeground(Color.WHITE);
		btnx.setFont(new Font("Arial Black", Font.BOLD, 20));
		btnx.setFocusPainted(false);
		btnx.setContentAreaFilled(false);
		btnx.setBorderPainted(false);
		btnx.setBounds(0, 0, 50, 50);
		contentPane.add(btnx);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(new Color(0, 102, 153));
		tabbedPane.setBounds(60, 11, 380, 278);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 102, 153));
		tabbedPane.addTab("Servers", null, panel, null);
		tabbedPane.setBackgroundAt(0, new Color(0, 102, 153));
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 375, 169);
		panel.add(scrollPane);
		
		this.model = new DefaultTableModel(new Object[][] {}, new String[] { "Name", "Host", "Connect" }) {
			/**
			 *
			 */
			private static final long serialVersionUID = 9096671483734523652L;
			
			@Override
			public boolean isCellEditable(final int row, final int column) {
				return column == 2;
			}
		};
		
		final Action packet = new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = -2431380960329863102L;
			
			public void actionPerformed(final ActionEvent e) {
				try {
					conq.save();
				} catch (IOException e1) {
					Main.handleError(e1);
				}
				final JTable table = (JTable) e.getSource();
				final int modelRow = Integer.valueOf(e.getActionCommand());
				System.out.println(table.getValueAt(modelRow, 0));
			}
		};
		
		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.setModel(this.model);
		scrollPane.setViewportView(this.table);
		
		new ButtonColumn(this.table, packet, 2);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Host", null, panel_1, null);
		panel_1.setLayout(null);
		
		textField = new JTextField("3033");
		textField.setBounds(10, 11, 355, 20);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Host");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GamePage.this.dispose();
				new Server(Integer.parseInt(textField.getText())).go();
			}
		});
		btnNewButton.setBounds(10, 42, 355, 23);
		panel_1.add(btnNewButton);
		
		new ButtonColumn(table,new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				final JTable table = (JTable) e.getSource();
				final int modelRow = Integer.valueOf(e.getActionCommand());
				GamePage.this.dispose();
				new Client(null,(String) table.getValueAt(modelRow, 1)).go();
			}},2);
		
		textField_1 = new JTextField();
		textField_1.setBorder(null);
		textField_1.setBounds(166, 216, 199, 23);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnAddServer = new JButton("Add Server");
		btnAddServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conq.pref.putIfAbsent("servers", new JSONArray());
				JSONArray ja=new JSONArray();
				ja.add(textField_2.getText());
				ja.add(textField_1.getText());
				((JSONArray)conq.pref.get("servers")).add(ja);
				GamePage.this.addC(textField_1.getText(), textField_2.getText());
				try {
					conq.save();
				} catch (IOException e1) {
					Main.handleError(e1);
				}
			}
		});
		btnAddServer.setBounds(10, 182, 355, 23);
		panel.add(btnAddServer);
		
		textField_2 = new JTextField();
		textField_2.setBorder(null);
		textField_2.setBounds(10, 216, 146, 23);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		if (servers!=null) {
		Iterator iter=servers.iterator();
		while (iter.hasNext()) {
			JSONArray jo=(JSONArray) iter.next();
			String name=(String) jo.get(0);
			String host=(String) jo.get(1);
			this.addC(host, name);
		}
		}
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			super.setVisible(true);
		for (float i=0;i<1;i+=0.01) {
			GamePage.this.setOpacity(i);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.exit(0);
			}
		}
		} else {
			for (float i=1;i>=0;i-=0.01) {
				GamePage.this.setOpacity(i);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.exit(0);
				}
			}
		}
	}
	
	public void dispose() {
		this.setVisible(false);
		super.dispose();
	}
	
	private void addC(String host,String name) {
		((DefaultTableModel) this.table.getModel()).addRow(new Object[] {name,host,"Connect"});
	}
}
