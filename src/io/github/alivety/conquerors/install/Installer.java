package io.github.alivety.conquerors.install;

import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Installer {
	public static void main(final String[] args) throws IOException, InterruptedException {
		final Path directory = Paths.get(System.getProperty("user.home"), "conquerors");
		final Path lib_dir = Paths.get(directory.toString(), "lib");
		
		final Path jar = Paths.get(directory.toString(), "conquerors.jar");
		
		if (!Files.exists(directory))
			Files.createDirectories(directory);
		if (!Files.exists(lib_dir))
			Files.createDirectories(lib_dir);
		
		if (!Files.exists(jar)) {
			final JDialog dia = Installer.mkmsg("<html>Downloading games<br>This might take a few minutes");
			Installer.download("https://github.com/alivety/The-Conquerors/blob/master/conquerors.jar?raw=true", jar);
			dia.dispose();
		}
		
		final Process p = Runtime.getRuntime().exec("java -jar " + jar.toAbsolutePath().toString());
		System.out.println(p);
		
		new Thread(new Runnable() {
			public void run() {
				final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null;
				
				try {
					while ((line = input.readLine()) != null)
						System.out.println(line);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				final BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;
				
				try {
					while ((line = input.readLine()) != null)
						System.out.println(line);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		System.out.println(p.waitFor());
	}
	
	private static void download(final String url, final Path output) throws IOException {
		System.out.println(url + " -> " + output);
		final URL website = new URL(url);
		final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		final FileOutputStream fos = new FileOutputStream(output.toFile());
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}
	
	private static JDialog mkmsg(final String msg) {
		final JDialog dialog = new JDialog((Frame) null, "Downloading");
		new Thread() {
			@Override
			public void run() {
				final JPanel pane = new JPanel();
				final JLabel label = new JLabel(msg);
				pane.add(label);
				dialog.add(pane);
				dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setLocationRelativeTo(null);
				dialog.pack();
				dialog.setVisible(true);
			}
		}.start();
		;
		
		return dialog;
	}
}
