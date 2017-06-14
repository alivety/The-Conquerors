package io.github.alivety.conquerors.common;

import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Throwables;

public class ErrorDialog extends JDialog {
	private static final long serialVersionUID = -4420240235644032632L;
	private final JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ErrorDialog(final Throwable e) {
		this.setTitle("Error");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 800, 400);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 38, 769, 312); // -31, -88
		this.contentPane.add(scrollPane);

		final JTextArea txtrHi = new JTextArea();
		txtrHi.setEditable(false);
		txtrHi.setText(Throwables.getStackTraceAsString(e.getCause())+"\r\n\r\n--- FULL LOGTRACE ---\r\n"+Throwables.getStackTraceAsString(e));
		scrollPane.setViewportView(txtrHi);

		final JLabel lblErrorMessage = new JLabel(e.getCause().getMessage() == null ? "A fatal error occured" : e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
		lblErrorMessage.setBounds(10, 13, 641, 14);
		this.contentPane.add(lblErrorMessage);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(final WindowEvent e) {
				ErrorDialog.this.doClose();
			}

			@Override
			public void windowClosing(final WindowEvent e) {
				ErrorDialog.this.doClose();
			}
		});

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
