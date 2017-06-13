package io.github.alivety.conquerors.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import io.github.alivety.conquerors.common.Main;

public class PacketListChooser extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -4960518377349899762L;
	private final JPanel contentPane;
	private final JLabel lblEnterThePacket;
	private final JTextField pid_tf;

	/**
	 * Create the frame.
	 */
	public PacketListChooser(final Test test) {
		this.setTitle("Packet Builder");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		this.lblEnterThePacket = new JLabel("Enter the Packet ID:");
		this.lblEnterThePacket.setBounds(10, 11, 123, 14);
		this.contentPane.add(this.lblEnterThePacket);

		this.pid_tf = new JTextField();
		this.pid_tf.setBounds(128, 8, 86, 20);
		this.contentPane.add(this.pid_tf);
		this.pid_tf.setColumns(10);

		final JButton btnBuildPacket = new JButton("Build Packet");
		btnBuildPacket.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				PacketBuilder pb = null;
				try {
					pb = new PacketBuilder(test, Main.getUnbuiltPacket(Integer.parseInt(PacketListChooser.this.pid_tf.getText())), true);
				} catch (final Exception e) {
					Main.handleError(e);
				}
				pb.setVisible(true);
			}
		});
		btnBuildPacket.setBounds(224, 7, 165, 23);
		this.contentPane.add(btnBuildPacket);
	}
}
