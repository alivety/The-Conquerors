package io.github.alivety.conquerors.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.github.alivety.conquerors.common.Main;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PacketListChooser extends JFrame {

	private JPanel contentPane;
	private JLabel lblEnterThePacket;
	private JTextField pid_tf;

	/**
	 * Create the frame.
	 */
	public PacketListChooser(final Test test) {
		setTitle("Packet Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblEnterThePacket = new JLabel("Enter the Packet ID:");
		lblEnterThePacket.setBounds(10, 11, 123, 14);
		contentPane.add(lblEnterThePacket);
		
		pid_tf = new JTextField();
		pid_tf.setBounds(128, 8, 86, 20);
		contentPane.add(pid_tf);
		pid_tf.setColumns(10);
		
		JButton btnBuildPacket = new JButton("Build Packet");
		btnBuildPacket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PacketBuilder pb=null;
				try {
					pb = new PacketBuilder(test,Main.getUnbuiltPacket(Integer.parseInt(pid_tf.getText())),true);
				} catch (Exception e) {
					Main.handleError(e);
				}
				pb.setVisible(true);
			}
		});
		btnBuildPacket.setBounds(224, 7, 165, 23);
		contentPane.add(btnBuildPacket);
	}
}
