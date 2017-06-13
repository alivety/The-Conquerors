package io.github.alivety.conquerors.common;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Throwables;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class ErrorDialog extends JDialog {
	
	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public ErrorDialog(Throwable e) {
		setTitle("Error");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 38, 569, 212); //-31, -88
		contentPane.add(scrollPane);
		
		JTextArea txtrHi = new JTextArea();
		txtrHi.setEditable(false);
		txtrHi.setText(Throwables.getStackTraceAsString(e));
		scrollPane.setViewportView(txtrHi);
		
		JLabel lblErrorMessage = new JLabel(e.getMessage()==null ? "A fatal error occured" : e.getClass().getSimpleName()+": "+e.getMessage());
		lblErrorMessage.setBounds(10, 13, 641, 14);
		contentPane.add(lblErrorMessage);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosed(final WindowEvent e) {
			ErrorDialog.this.doClose();
		}
			
		public void windowClosing(final WindowEvent e) {
			ErrorDialog.this.doClose();
		}});
		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
	}
	
	private void doClose() {
		if (Desktop.isDesktopSupported()) {
			Main.out.debug("desktop is supported");
			try {
				Desktop.getDesktop().open(Main.out.file());
			} catch (final IOException e) {
				e.printStackTrace();
			}
			;
		}
		System.exit(0);
	}
}
