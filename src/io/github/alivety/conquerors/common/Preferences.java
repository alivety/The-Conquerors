package io.github.alivety.conquerors.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.github.alivety.conquerors.client.Client;

public class Preferences {
	private final Conquerors conq;
	
	public Preferences(final JSONObject prefs) throws IOException, ParseException {
		JFrame.setDefaultLookAndFeelDecorated(false);
		conq=new Conquerors();
	}
	
	public void setVisible(boolean visible) {
		conq.setVisible(visible);
	}
	
	public String getUsername() {
		return (String) conq.pref.get("name");
	}
	
	private static class Conquerors extends JFrame {
		protected final JSONObject pref;
		
		private JPanel contentPane;
		private JTextField txtUsername;
		private JPasswordField passwordField;
		private JButton btnNewButton_1;
		@SuppressWarnings("unchecked")
		public Conquerors() throws FileNotFoundException, IOException, ParseException {
			JSONParser parser=new JSONParser();
			this.pref=(JSONObject)parser.parse(new FileReader(Main.USER_PREFS));
			setForeground(Color.WHITE);
			setBackground(new Color(0, 102, 153));
			setUndecorated(true);
			setTitle("The Conquerors");
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			contentPane = new JPanel();
			contentPane.setBackground(new Color(0, 102, 153));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JButton btnNewButton = new JButton("<html><b>X</b>");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Conquerors.this.setVisible(false);
					System.exit(0);
				}
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
			contentPane.add(btnNewButton);
			
			txtUsername = new JTextField();
			txtUsername.setBorder(new EmptyBorder(0, 0, 0, 0));
			txtUsername.setText((String) pref.getOrDefault("name", "Username"));
			txtUsername.setFont(new Font("Dialog", Font.PLAIN, 14));
			txtUsername.setBounds(10, 111, 430, 35);
			contentPane.add(txtUsername);
			txtUsername.setColumns(10);
			
			passwordField = new JPasswordField();
			passwordField.setFont(new Font("Dialog", Font.PLAIN, 14));
			passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
			passwordField.setVisible(true);
			passwordField.setBounds(10, 185, 430, 35);
			contentPane.add(passwordField);
			
			btnNewButton_1 = new JButton("Login");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pref.put("name", txtUsername.getText());
					Conquerors.this.setVisible(false);
					new Client(null).go();
				}
			});
			btnNewButton_1.setFont(new Font("Dialog", Font.PLAIN, 14));
			btnNewButton_1.setBounds(10, 266, 430, 23);
			contentPane.add(btnNewButton_1);
		}
		
		public void setVisible(boolean visible) {
			if (visible) {
				super.setVisible(true);
			for (float i=0;i<1;i+=0.01) {
				Conquerors.this.setOpacity(i);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.exit(0);
				}
			}
			} else {
				for (float i=1;i>=0;i-=0.01) {
					Conquerors.this.setOpacity(i);
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						System.exit(0);
					}
				}
			}
		}
	}
}
