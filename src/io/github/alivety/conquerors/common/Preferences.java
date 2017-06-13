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
		public Mode() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("GameMode");
			JPanel pane=new JPanel();
			JButton client=new JButton("Client");
			client.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Client().go();
				}});
			JButton server=new JButton("Server");
			server.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Server().go();
				}});
			JButton test=new JButton("Manual");
			test.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Test().go();
				}});
			pane.add(client);pane.add(server);pane.add(test);
			add(pane);
			pack();
			setLocationRelativeTo(null);
		}
	}
	private final JSONObject prefs;
	public String username;
	public final JSONObject servers=null;
	public Preferences(JSONObject prefs) throws IOException {
		this.prefs=prefs;
		JPanel pane=new JPanel();
		pane.add(new JLabel("Username: "));
		final JTextField username=new JTextField((String) prefs.getOrDefault("name", ""));
		username.setColumns(20);
		pane.add(username);
		JButton btn=new JButton("Login");
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Preferences.this.username=username.getText();
				new Mode().setVisible(true);
				Preferences.this.dispose();
			}});
		pane.add(btn);
		add(pane);
		
		pack();
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login");
	}
	
	public void save() throws IOException {
		prefs.put("name", username);
		prefs.put("servers", servers);
		PrintWriter pw=new PrintWriter(Main.USER_PREFS);
		prefs.writeJSONString(pw);
		pw.flush();
		pw.close();
	}
}
