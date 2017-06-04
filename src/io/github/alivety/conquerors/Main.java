package io.github.alivety.conquerors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Throwables;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.server.Server;

public class Main {
	public static Logger out;
	public static void main(String[]args) {
		try {
		System.setProperty("log4j.configurationFile","configuration.xml");
		///Configurator.setRootLevel(Level.INFO);
		int c=JOptionPane.showOptionDialog(null, "Are you hosting a server or playing the game?", "Choose", 0, 0, null, new Object[]{"Server","Client"}, 0);
		if (c==0) {//Server
			Server server=new Server();
			server.go();
		} else if (c==1 ){//Client
			Client client=new Client();
			client.go();
		} else {
			out=LogManager.getLogger("undefined");
			throw new IllegalStateException("");
		}
		} catch (Exception e) {
			Main.handleError(e);
		}
	}
	
	public static void handleError(Throwable t) {
		out.error(Throwables.getStackTraceAsString(t));
		ErrorDialog ed=new ErrorDialog("Unexpected system error", "An uncaught error occured", t);
		ed.setVisible(true);
	}
	
	public static void setupLogger(ConquerorsApp app) {
		out=LogManager.getLogger(app.getClass().getSimpleName());
	}
}
