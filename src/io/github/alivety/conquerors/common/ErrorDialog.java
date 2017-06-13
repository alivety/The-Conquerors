package io.github.alivety.conquerors.common;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Throwables;

public class ErrorDialog extends JFrame {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -1358232705330232980L;
	private final int dialogWidth = 500;
	private final int dialogHeight = 140;
	
	private final JLabel iconLabel = new JLabel();
	
	// is error panel opened up
	private boolean open = false;
	
	private final JLabel errorLabel = new JLabel();
	private final JTextArea errorTextArea = new JTextArea("");
	
	private final JTextArea exceptionTextArea = new JTextArea("");
	private JScrollPane exceptionTextAreaSP = new JScrollPane();
	
	private final JButton okButton = new JButton("OK");
	private final JButton viewButton = new JButton("View Error");
	// private final JButton emailButton = new JButton("Email Error");
	
	private final JPanel topPanel = new JPanel(new BorderLayout());
	
	public ErrorDialog(final String errorLabelText, final String errorDescription, final Throwable e) {
		
		final StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		if (e.getCause() != null) {
			e.getCause().printStackTrace(new PrintWriter(errors));
		}
		
		this.setSize(this.dialogWidth, this.dialogHeight);
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.errorTextArea.setText(errorDescription);
		
		this.errorLabel.setText(errorLabelText);
		
		this.exceptionTextArea.setText(Throwables.getStackTraceAsString(e));
		
		this.exceptionTextAreaSP = new JScrollPane(this.exceptionTextArea);
		
		this.iconLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		this.iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		this.setupUI();
		
		this.setUpListeners();
	}
	
	public ErrorDialog(final String errorLabelText, final Throwable e) {
		this(errorLabelText, null, e);
	}
	
	public void setupUI() {
		
		this.setTitle("Error");
		
		this.errorTextArea.setLineWrap(true);
		this.errorTextArea.setWrapStyleWord(true);
		this.errorTextArea.setEditable(false);
		
		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		buttonPanel.add(this.okButton);
		// buttonPanel.add(emailButton);
		buttonPanel.add(this.viewButton);
		
		this.errorTextArea.setBackground(this.iconLabel.getBackground());
		
		final JScrollPane textAreaSP = new JScrollPane(this.errorTextArea);
		
		textAreaSP.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		
		this.errorLabel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		
		this.exceptionTextArea.setPreferredSize(new Dimension(100, 100));
		
		this.topPanel.add(this.iconLabel, BorderLayout.WEST);
		
		final JPanel p = new JPanel(new BorderLayout());
		p.add(this.errorLabel, BorderLayout.NORTH);
		p.add(textAreaSP);
		
		this.topPanel.add(p);
		
		this.add(this.topPanel);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void setUpListeners() {
		
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				ErrorDialog.this.doClose();
			}
		});
		
		this.viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				if (ErrorDialog.this.open) {
					ErrorDialog.this.viewButton.setText("View Error");
					
					ErrorDialog.this.topPanel.remove(ErrorDialog.this.exceptionTextAreaSP);
					
					ErrorDialog.this.setSize(ErrorDialog.this.dialogWidth, ErrorDialog.this.dialogHeight);
					
					ErrorDialog.this.topPanel.revalidate();
					
					ErrorDialog.this.open = false;
					
				} else {
					
					ErrorDialog.this.viewButton.setText("Hide Error");
					
					ErrorDialog.this.topPanel.add(ErrorDialog.this.exceptionTextAreaSP, BorderLayout.SOUTH);
					
					ErrorDialog.this.setSize(ErrorDialog.this.dialogWidth, ErrorDialog.this.dialogHeight + 100);
					
					ErrorDialog.this.topPanel.revalidate();
					
					ErrorDialog.this.open = true;
				}
			}
		});
		
		this.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowClosed(final WindowEvent e) {
				ErrorDialog.this.doClose();
			}
			
			@Override
			public void windowClosing(final WindowEvent e) {
				ErrorDialog.this.doClose();
			}
		});
	}
	
	private void doClose() {
		if (Desktop.isDesktopSupported()) {
			Main.out.debug("desktop is supported");
			try {
				Desktop.getDesktop().open(Main.out.file());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} ;
		}
		System.exit(0);
	}
}