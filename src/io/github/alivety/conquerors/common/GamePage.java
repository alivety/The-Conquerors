package io.github.alivety.conquerors.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.simple.JSONArray;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.test.ButtonColumn;

public class GamePage extends JFrame {
	
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private TableModel model;
	private JTextField textField_1;
	private JTextField textField_2;
	
	public GamePage(final Preferences.Conquerors conq, final JSONArray servers) {
		this.setTitle("The Conquerors");
		this.setUndecorated(true);
		this.setResizable(false);
		this.setBackground(new Color(0, 102, 153));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(new Color(0, 102, 153));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		
		final JButton btnx = new JButton("<html><b>X");
		btnx.addActionListener(e -> {
			try {
				conq.save();
			} catch (final IOException e1) {
				Main.handleError(e1);
			}
			GamePage.this.setVisible(false);
			System.exit(0);
		});
		btnx.setForeground(Color.WHITE);
		btnx.setFont(new Font("Arial Black", Font.BOLD, 20));
		btnx.setFocusPainted(false);
		btnx.setContentAreaFilled(false);
		btnx.setBorderPainted(false);
		btnx.setBounds(0, 0, 50, 50);
		this.contentPane.add(btnx);
		
		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBackground(new Color(0, 102, 153));
		tabbedPane.setBounds(60, 11, 380, 278);
		this.contentPane.add(tabbedPane);
		
		final JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 102, 153));
		tabbedPane.addTab("Servers", null, panel, null);
		tabbedPane.setBackgroundAt(0, new Color(0, 102, 153));
		panel.setLayout(null);
		
		final JScrollPane scrollPane = new JScrollPane();
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
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					conq.save();
				} catch (final IOException e1) {
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
		scrollPane.setViewportView(this.table);
		
		final JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Host", null, panel_1, null);
		panel_1.setLayout(null);
		
		this.textField = new JTextField("3033");
		this.textField.setBounds(10, 11, 355, 20);
		panel_1.add(this.textField);
		this.textField.setColumns(10);
		
		final JButton btnNewButton = new JButton("Host");
		btnNewButton.addActionListener(e -> {
			GamePage.this.dispose();
			new Server(Integer.parseInt(GamePage.this.textField.getText())).go();
		});
		btnNewButton.setBounds(10, 42, 355, 23);
		panel_1.add(btnNewButton);
		
		new ButtonColumn(this.table, new AbstractAction() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JTable table = (JTable) e.getSource();
				final int modelRow = Integer.valueOf(e.getActionCommand());
				GamePage.this.dispose();
				new Client(null, (String) table.getValueAt(modelRow, 1)).go();
			}
		}, 2);
		
		this.textField_1 = new JTextField();
		this.textField_1.setBorder(null);
		this.textField_1.setBounds(166, 216, 199, 23);
		panel.add(this.textField_1);
		this.textField_1.setColumns(10);
		
		final JButton btnAddServer = new JButton("Add Server");
		btnAddServer.addActionListener(e -> {
			conq.pref.putIfAbsent("servers", new JSONArray());
			final JSONArray ja = new JSONArray();
			ja.add(GamePage.this.textField_2.getText());
			ja.add(GamePage.this.textField_1.getText());
			((JSONArray) conq.pref.get("servers")).add(ja);
			GamePage.this.addC(GamePage.this.textField_1.getText(), GamePage.this.textField_2.getText());
			try {
				conq.save();
			} catch (final IOException e1) {
				Main.handleError(e1);
			}
		});
		btnAddServer.setBounds(10, 182, 355, 23);
		panel.add(btnAddServer);
		
		this.textField_2 = new JTextField();
		this.textField_2.setBorder(null);
		this.textField_2.setBounds(10, 216, 146, 23);
		panel.add(this.textField_2);
		this.textField_2.setColumns(10);
		
		if (servers != null) {
			final Iterator iter = servers.iterator();
			while (iter.hasNext()) {
				final JSONArray jo = (JSONArray) iter.next();
				final String name = (String) jo.get(0);
				final String host = (String) jo.get(1);
				this.addC(host, name);
			}
		}
	}
	
	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			super.setVisible(true);
			for (float i = 0; i < 1; i += 0.01) {
				GamePage.this.setOpacity(i);
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					System.exit(0);
				}
			}
		} else
			for (float i = 1; i >= 0; i -= 0.01) {
				GamePage.this.setOpacity(i);
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					System.exit(0);
				}
			}
	}
	
	@Override
	public void dispose() {
		this.setVisible(false);
		super.dispose();
	}
	
	private void addC(final String host, final String name) {
		((DefaultTableModel) this.table.getModel()).addRow(new Object[] { name, host, "Connect" });
	}
}
