package io.github.alivety.conquerors.install;

import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class Installer {
	public static void main(String[]args) throws IOException, InterruptedException {
		Path directory=Paths.get(System.getProperty("user.home"), "conquerors");
		Path lib_dir=Paths.get(directory.toString(),"lib");
		
		Path lib_rar=Paths.get(directory.toString(),"lib.rar");
		Path jar=Paths.get(directory.toString(),"conquerors.jar");
		
		if (!Files.exists(directory))
			Files.createDirectories(directory);
		if (!Files.exists(lib_dir))
			Files.createDirectories(lib_dir);
		
		if (!Files.exists(jar)) {
			JDialog dia=mkmsg("<html>Downloading games<br>This might take a few minutes");
			download("https://github.com/alivety/The-Conquerors/blob/master/conquerors.jar?raw=true",jar);
			dia.dispose();
		}
		
		final Process p = Runtime.getRuntime().exec("java -jar "+jar.toAbsolutePath().toString());
		System.out.println(p);

		new Thread(new Runnable() {
		    public void run() {
		     BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		     String line = null; 

		     try {
		        while ((line = input.readLine()) != null)
		            System.out.println(line);
		     } catch (IOException e) {
		            e.printStackTrace();
		     }
		    }
		}).start();
		
		new Thread(new Runnable() {
		    public void run() {
		     BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		     String line = null; 

		     try {
		        while ((line = input.readLine()) != null)
		            System.out.println(line);
		     } catch (IOException e) {
		            e.printStackTrace();
		     }
		    }
		}).start();

		System.out.println(p.waitFor());
	}
	
	private static void download(String url,Path output) throws IOException {
		System.out.println(url +" -> "+output);
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(output.toFile());
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}
	
	private static JDialog mkmsg(String msg) {
		final JDialog dialog=new JDialog((Frame)null, "Downloading");
		new Thread(){
		@Override
		public void run() {
			JPanel pane=new JPanel();
			JLabel label=new JLabel(msg);
			pane.add(label);
			dialog.add(pane);
			dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setLocationRelativeTo(null);
			dialog.pack();
			dialog.setVisible(true);
		}}.start();;
		
		
		return dialog;
	}
}
