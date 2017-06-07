package io.github.alivety.conquerors;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Throwables;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.event.Event;
import io.github.alivety.conquerors.event.EventBus;
import io.github.alivety.conquerors.event.PacketResolver;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PPL;

public class Main {
	public static final int PRO_VER=0;
	public static final Random random=new Random();
	public static Logger out;
	public static final EventBus EVENT_BUS=new EventBus();
	public static int server=0;
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
		Vector v1=Main.nv(50,674,23);
		Vector v2=Main.nv(23,64,53);
		System.out.println(v1 +"~"+v2+" = "+v1.distance(v2));
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
			out=Logger.getLogger("undefined");
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
		out=Logger.getLogger(app.getClass().getSimpleName().toLowerCase());
		out.info("setup");
		out.info(System.getProperties());
		out.info(System.getenv());
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
	
	public static String uuid(String object) {
		return object+"["+Main.random.nextInt()+"]";
	}
	
	public static String formatChatMessage(String sender,String msg) {
		SimpleDateFormat df=new SimpleDateFormat("k:m");
		String date=df.format(new Date());
		if (sender==null) {
			return msg+" ("+date+")";
		} else {
			return sender+": "+msg+" ("+date+")";
		}
	}
	
	public static String formatChatMessage(String msg) {
		return formatChatMessage(null,msg);
	}
	
	public static Vector nv(float x,float y,float z) {
		return new Vector(x,y,z);
	}

	public static final PacketResolver resolver=new PacketResolver();
}
