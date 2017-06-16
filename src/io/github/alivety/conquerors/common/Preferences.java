package io.github.alivety.conquerors.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.test.Test;

public class Preferences extends JFrame {
	private static class Mode extends JFrame {
		/**
		 *
		 */
		private static final long serialVersionUID = 4546426182193116949L;

		public Mode() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("GameMode");
			final JPanel pane = new JPanel();
			final JButton client = new JButton("Client");
			client.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					Mode.this.dispose();
					new Client(null).go();
				}
			});
			final JButton server = new JButton("Server");
			server.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					Mode.this.dispose();
					new Server().go();
				}
			});
			final JButton test = new JButton("Manual");
			test.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					Mode.this.dispose();
					new Test().go();
				}
			});
			pane.add(client);
			pane.add(server);
			pane.add(test);
			this.add(pane);
			this.pack();
			this.setLocationRelativeTo(null);
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -4729521272308604150L;

	private final JSONObject prefs;
	public String username;
	public final JSONObject servers = null;

	public Preferences(final JSONObject prefs) throws IOException {
		this.prefs = prefs;
		final JPanel pane = new JPanel();
		pane.add(new JLabel("Username: "));
		@SuppressWarnings("unchecked")
		final JTextField username = new JTextField((String) prefs.getOrDefault("name", ""));
		username.setColumns(20);
		pane.add(username);
		final JButton btn = new JButton("Login");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Preferences.this.username = username.getText();
				new Mode().setVisible(true);
				Preferences.this.dispose();
			}
		});
		pane.add(btn);
		this.add(pane);

		this.pack();
		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Login");
	}

	@SuppressWarnings("unchecked")
	public void save() throws IOException {
		this.prefs.put("name", this.username);
		this.prefs.put("servers", this.servers);
		final PrintWriter pw = new PrintWriter(Main.USER_PREFS);
		this.prefs.writeJSONString(pw);
		pw.flush();
		pw.close();
	}

	public String getUsername() {
		return this.username;
	}
}
