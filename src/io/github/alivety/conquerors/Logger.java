package io.github.alivety.conquerors;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Logger extends PrintStream {
	private final String name;
	private Logger(String name) {
		super(System.out);
		this.name=name;
	}
	
	private static final SimpleDateFormat df=new SimpleDateFormat("kk:mm:ss.SSS");
	private static final HashMap<String,Logger> loggers=new HashMap<String,Logger>();
	public static Logger getLogger(String name) {
		if (loggers.containsKey(name)) return loggers.get(name);
		Logger l=new Logger(name);
		loggers.put(name, l);
		return l;
	}
	
	private String format(Level level,Object msg) {
		return df.format(new Date())+" ["+Thread.currentThread().getName()+"] "+level.name()+" "+name+" - "+msg;
	}
	
	private synchronized void print(Level level,Object msg) {
		System.out.println(this.format(level, msg));
	}
	
	public void info(Object o) {
		this.print(Level.INFO,o);
	}
	
	public void debug(Object o) {
		this.print(Level.DEBUG,o);
	}
	
	public void warn(Object o) {
		this.print(Level.WARNING,o);
	}
	
	public void error(Object o) {
		this.print(Level.ERROR,o);
	}
	
	private enum Level {
		INFO,DEBUG,WARNING,ERROR;
		
	}
}
