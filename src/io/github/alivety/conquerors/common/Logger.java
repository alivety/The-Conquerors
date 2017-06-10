package io.github.alivety.conquerors.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Logger extends PrintStream {
	private final String name;
	private final PrintWriter fout;

	private Logger(final String name) throws IOException {
		super(System.out);
		this.name = name;
		final File f = new File("logs/conquerors-" + Logger.df.format(new Date()).replace(":", ".") + ".log");
		f.createNewFile();
		this.fout = new PrintWriter(f);
	}

	private static final SimpleDateFormat df = new SimpleDateFormat("kk:mm:ss.SSS");
	private static final HashMap<String, Logger> loggers = new HashMap<String, Logger>();

	public static Logger getLogger(final String name) throws IOException {
		if (Logger.loggers.containsKey(name))
			return Logger.loggers.get(name);
		final Logger l = new Logger(name);
		Logger.loggers.put(name, l);
		return l;
	}

	private String format(final Level level, final Object msg) {
		return Logger.df.format(new Date()) + " [" + Thread.currentThread().getName() + "] " + level.name() + " "
				+ this.name + " - " + msg;
	}

	private synchronized void print(final Level level, final Object msg) {
		System.out.println(this.format(level, msg));
		this.fout.println(this.format(level, msg));
		this.fout.flush();
	}

	public void info(final Object o) {
		this.print(Level.INFO, o);
	}

	public void debug(final Object o) {
		this.print(Level.DEBUG, o);
	}

	public void warn(final Object o) {
		this.print(Level.WARNING, o);
	}

	public void error(final Object o) {
		this.print(Level.ERROR, o);
	}

	private enum Level {
		INFO, DEBUG, WARNING, ERROR;

	}
}