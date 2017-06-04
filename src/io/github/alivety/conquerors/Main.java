package io.github.alivety.conquerors;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PPL;

public class Main {
	public static Logger out;
	private static Map<String, Object> asMap(Object... args) {
	    Map<String, Object> argMap = new HashMap<String, Object>();
	    for (int i = 0; i < args.length; i += 2) {
	      String key;
	      try {
	        key = (String) args[i];
	      } catch (ClassCastException cce) {
	        System.err.println(cce.getMessage());
	        System.err.println("args[" + i + "] " + args[i].toString());
	        throw cce;
	      }
	      if (i + 1 < args.length) {
	        Object value = args[i + 1];
	        argMap.put(key, value);
	      }
	    }
	    return argMap;
	  }
	public static void main(String[]args) {
		try {
		Map<String, Object> arg=asMap(args);
		System.setProperty("log4j.configurationFile","configuration.xml");
		int c=/*JOptionPane.showOptionDialog(null, "Are you hosting a server or playing the game?", "Choose", 0, 0, null, new Object[]{"Server","Client"}, 0)*/1;
		if (arg.containsKey("server")) c=0;
		if (c==0) {//Server
			Server server=new Server();
			server.go();
		} else if (c==1 ){//Client
			Client client=new Client();
			client.go();
		} else {
			out=LogManager.getLogger("undefined");
			throw new IllegalStateException("c-value must be either 0 or 1");
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
	
	public static AbstractPacket decode(ByteBuffer buf) {
		try {
			return AbstractPacket.decode("p.P", buf);
		} catch (Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static ByteBuffer encode(AbstractPacket p) {
		try {
			return PPL.encapsulate(p.encode());
		} catch (Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static AbstractPacket createPacket(int id, Object... fields) {
		try {
			return AbstractPacket.c("p.P"+id, fields);
		} catch (Exception e) {
			Main.handleError(e);
			return null;
		}
	}
}
