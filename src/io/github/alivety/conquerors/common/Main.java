package io.github.alivety.conquerors.common;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.swing.JFrame;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.base.Throwables;
import com.jme3.math.Vector3f;

import io.github.alivety.conquerors.common.event.EventBus;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.ppl.PPL;
import io.github.alivety.ppl.packet.Packet;

public class Main {
	public static final int PRO_VER = 0;
	public static final Random random = new Random();
	public static Logger out;
	public static final EventBus EVENT_BUS = new EventBus();
	public static final PPLListener PACKET_CATCHER=new PPLListener();
	public static final PacketResolver resolver = new PacketResolver();
	public static int server = 0;
	private @Deprecated static final String PACKET_LOCATION = "io.github.alivety.conquerors.common.packets.P";
	public static final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
	public static final File USER_PREFS = new File("prefs.json");
	public static Preferences PREFS;
	
	public static Packet createPacket(int id,Object... fields) {
		Packet packet=newPacket(id,fields);
		PACKET_CATCHER.addRow(new Object[] {(server==1)?"Server":"Client",id,packet});
		return packet;
	}
	
    private static Packet newPacket(final int id, final Object... fields) {
		try {
			return PPL.newInstance(id, fields);
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static Packet decode(final ByteBuffer buf) {
		try {
			return PPL.decode(buf);
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static ByteBuffer encode(final Packet p) {
		try {
			return PPL.encode(p);
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static String formatChatMessage(final String msg) {
		return Main.formatChatMessage(null, msg);
	}
	
	public static String formatChatMessage(final String sender, final String msg) {
		final SimpleDateFormat df = new SimpleDateFormat("k:m");
		final String date = df.format(new Date());
		if (sender == null)
			return msg + " (" + date + ")";
		else
			return sender + ": " + msg + " (" + date + ")";
	}
	
	public static Packet getUnbuiltPacket(final int id) {
		try {
			return PPL.getUnbuiltPacket(id).newInstance();
		} catch (final Exception e) {
			Main.handleError(e);
			return null;
		}
	}
	
	public static void handleError(final Throwable t) {
		try {
			throw new LogTraceException(t);
		} catch (final LogTraceException e) {
			if (Main.out != null)
				Main.out.error(Throwables.getStackTraceAsString(e));
			else
				t.printStackTrace(System.out);
			
			final ErrorDialog ed = new ErrorDialog(e);
			ed.setVisible(true);
		}
	}
	
	private static class LogTraceException extends Exception {
		public LogTraceException(final Throwable t) {
			super(t.getMessage(), t);
		}
		
		private static final long serialVersionUID = 1304222821855174255L;
	}
	
	public static void main(final String[] args) throws IOException {
		JFrame.setDefaultLookAndFeelDecorated(false);
		Main.out = Logger.getLogger("undefined");
		try {
			if (Main.USER_PREFS.createNewFile()) {
				final FileWriter fw = new FileWriter(Main.USER_PREFS);
				fw.write(new JSONObject().toJSONString());
				fw.flush();
				fw.close();
			}
			
			PPL.loadPackets();
			
			final JSONParser parser = new JSONParser();
			Main.PREFS = new Preferences((JSONObject) parser.parse(new FileReader(Main.USER_PREFS)));
			Main.PREFS.setVisible(true);
		} catch (final Exception e) {
			Main.handleError(e);
		}
	}
	
	public static void setupLogger(final ConquerorsApp app) throws IOException {
		Main.out = Logger.getLogger(app.getClass().getSimpleName().toLowerCase());
		Main.out.info("setup");
		Main.out.debug(System.getProperties());
		Main.out.debug(System.getenv());
	}
	
	public static String uuid(final String object) {
		return object + "[" + Main.random.nextInt() + "]";
	}
	
	public static String vardump(@Nonnull final Object target) throws IllegalArgumentException, IllegalAccessException {
		final Class<?> cls = target.getClass();
		final Field[] fields = cls.getDeclaredFields();
		final StringBuilder sb = new StringBuilder();
		sb.append(cls.getSimpleName()).append("{");
		for (final Field f : fields) {
			f.setAccessible(true);
			sb.append(f.getName()).append("=").append(f.get(target)).append(";");
		}
		String s = sb.toString();
		s = s.substring(0, s.lastIndexOf(";"));
		return s + "}";
	}
}
