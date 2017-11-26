package io.github.alivety.conquerors.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Preferences {
	private final Conquerors conq;
	
	public Preferences(final JSONObject prefs) throws IOException, ParseException {
		JFrame.setDefaultLookAndFeelDecorated(false);
		this.conq = new Conquerors();
	}
	
	public void setVisible(final boolean visible) {
		this.conq.setVisible(visible);
	}
	
	public String getUsername() {
		return (String) this.conq.pref.get("name");
	}
	
	public static class Conquerors extends JFrame {
		protected final JSONObject pref;
		
		private final JPanel contentPane;
		private final JTextField txtUsername;
		private final JPasswordField passwordField;
		private final JButton btnNewButton_1;
		
		@SuppressWarnings("unchecked")
		public Conquerors() throws FileNotFoundException, IOException, ParseException {
			final JSONParser parser = new JSONParser();
			this.pref = (JSONObject) parser.parse(new FileReader(Main.USER_PREFS));
			this.setForeground(Color.WHITE);
			this.setBackground(new Color(0, 102, 153));
			this.setUndecorated(true);
			this.setTitle("The Conquerors");
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setBounds(100, 100, 450, 300);
			this.contentPane = new JPanel();
			this.contentPane.setBackground(new Color(0, 102, 153));
			this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.setContentPane(this.contentPane);
			this.contentPane.setLayout(null);
			
			final JButton btnNewButton = new JButton("<html><b>X</b>");
			btnNewButton.addActionListener(arg0 -> {
				Conquerors.this.setVisible(false);
				System.exit(0);
			});
			btnNewButton.setFocusPainted(false);
			btnNewButton.setBorder(new EmptyBorder(0, 0, 0, 0));
			btnNewButton.setMargin(new Insets(0, 0, 0, 0));
			btnNewButton.setFont(new Font("Arial Black", Font.BOLD, 20));
			btnNewButton.setIconTextGap(0);
			btnNewButton.setForeground(Color.WHITE);
			btnNewButton.setDefaultCapable(false);
			btnNewButton.setContentAreaFilled(false);
			btnNewButton.setBounds(0, 0, 50, 50);
			this.contentPane.add(btnNewButton);
			
			this.txtUsername = new JTextField();
			this.txtUsername.setBorder(new EmptyBorder(0, 0, 0, 0));
			this.txtUsername.setText((String) this.pref.getOrDefault("name", "Username"));
			this.txtUsername.setFont(new Font("Dialog", Font.PLAIN, 14));
			this.txtUsername.setBounds(10, 111, 430, 35);
			this.contentPane.add(this.txtUsername);
			this.txtUsername.setColumns(10);
			
			this.passwordField = new JPasswordField();
			this.passwordField.setFont(new Font("Dialog", Font.PLAIN, 14));
			this.passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
			this.passwordField.setVisible(false);
			this.passwordField.setBounds(10, 185, 430, 35);
			this.contentPane.add(this.passwordField);
			
			this.btnNewButton_1 = new JButton("Login");
			this.btnNewButton_1.addActionListener(e -> {
				Conquerors.this.pref.put("name", Conquerors.this.txtUsername.getText());
				Conquerors.this.dispose();
				new GamePage(Conquerors.this, (JSONArray) Conquerors.this.pref.get("servers")).setVisible(true);
			});
			this.btnNewButton_1.setFont(new Font("Dialog", Font.PLAIN, 14));
			this.btnNewButton_1.setBounds(10, 266, 430, 23);
			this.contentPane.add(this.btnNewButton_1);
		}
		
		@Override
		public void setVisible(final boolean visible) {
			if (visible) {
				super.setVisible(true);
				for (float i = 0; i < 1; i += 0.01) {
					Conquerors.this.setOpacity(i);
					try {
						Thread.sleep(1);
					} catch (final InterruptedException e) {
						System.exit(0);
					}
				}
			} else
				for (float i = 1; i >= 0; i -= 0.01) {
					Conquerors.this.setOpacity(i);
					try {
						Thread.sleep(1);
					} catch (final InterruptedException e) {
						System.exit(0);
					}
				}
		}
		
		@Override
		public void dispose() {
			try {
				this.save();
			} catch (final IOException e) {
				Main.handleError(e);
			}
			this.setVisible(false);
			super.dispose();
		}
		
		public void save() throws IOException {
			final FileWriter fw = new FileWriter(Main.USER_PREFS);
			fw.write(this.pref.toJSONString());
			fw.flush();
			fw.close();
		}
	}
}
