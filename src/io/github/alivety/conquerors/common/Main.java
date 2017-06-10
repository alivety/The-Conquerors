package io.github.alivety.conquerors.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.common.base.Throwables;

import io.github.alivety.conquerors.client.Client;
import io.github.alivety.conquerors.common.event.EventBus;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.test.Test;
import io.github.alivety.ppl.PPL;
import io.github.alivety.ppl.Packet;

public class Main {
	public static final int PRO_VER = 0;
	public static final Random random = new Random();
	public static Logger out;
	public static final EventBus EVENT_BUS = new EventBus();
	public static int server = 0;
	private static final String PACKET_LOCATION = "io.github.alivety.conquerors.common.packets.P";
	public static final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

	private static Map<String, Object> asMap(final Object... args) {
		final Map<String, Object> argMap = new HashMap<String, Object>();
		for (int i = 0; i < args.length; i += 2) {
			String key;
			try {
				key = (String) args[i];
			} catch (final ClassCastException cce) {
				System.err.println(cce.getMessage());
				System.err.println("args[" + i + "] " + args[i].toString());
				throw cce;
			}
			if ((i + 1) < args.length) {
				final Object value = args[i + 1];
				argMap.put(key, value);
			}
		}
		return argMap;
	}

	public static void main(final String[] args) throws IOException {
		Main.out = Logger.getLogger("undefined");
		try {
			final Map<String, Object> arg = Main.asMap(args);
			System.setProperty("log4j.configurationFile", "configuration.xml");
			int c = /*
					 * JOptionPane.showOptionDialog(null,
					 * "Are you hosting a server or playing the game?",
					 * "Choose", 0, 0, null, new Object[]{"Server","Client"}, 0)
					 */2;
			if (arg.containsKey("server"))
				c = 0;
			if (c == 0) {// Server
				final Server server = new Server();
				server.go();
			} else if (c == 1) {// Client
				final Client client = new Client();
				client.go();
			} else if (c==2) {
				new Test().go();
			} else
				throw new IllegalStateException("c-value must be either 0 or 1");
		} catch (final Exception e) {
			Main.handleError(e);
		}
	}

	public static void handleError(final Throwable t) {
		if (Main.out != null)
			Main.out.error(Throwables.getStackTraceAsString(t));
		else
			t.printStackTrace(System.out);
		final ErrorDialog ed = new ErrorDialog("Unexpected system error", "An uncaught error occured", t);
		ed.setVisible(true);
	}

	public static void setupLogger(final ConquerorsApp app) throws IOException {
		Main.out = Logger.getLogger(app.getClass().getSimpleName().toLowerCase());
		Main.out.info("setup");
		Main.out.info(System.getProperties());
		Main.out.info(System.getenv());
	}

	public static Packet decode(final ByteBuffer buf) {
		try {
			return Packet.decode(Main.PACKET_LOCATION, buf);
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}

	public static ByteBuffer encode(final Packet p) {
		try {
			return PPL.encapsulate(p.encode());
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}

	public static Packet createPacket(final int id, final Object... fields) {
		try {
			return Packet.c(Main.PACKET_LOCATION + id, fields);
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}

	public static String uuid(final String object) {
		return object + "[" + Main.random.nextInt() + "]";
	}

	public static String formatChatMessage(final String sender, final String msg) {
		final SimpleDateFormat df = new SimpleDateFormat("k:m");
		final String date = df.format(new Date());
		if (sender == null)
			return msg + " (" + date + ")";
		else
			return sender + ": " + msg + " (" + date + ")";
	}

	public static String formatChatMessage(final String msg) {
		return Main.formatChatMessage(null, msg);
	}

	public static Vector nv(final float x, final float y, final float z) {
		return new Vector(x, y, z);
	}

	public static List<Class<?>> getSuperclassesOf(final Object target) {
		return null;
	}

	public static final PacketResolver resolver = new PacketResolver();
}
