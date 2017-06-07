package io.github.alivety.conquerors;

import java.util.Arrays;

public class Task {
	private final String name;
	private final JavaFunction func;
	private int i=0;
	public Task(String name,JavaFunction func) {
		this.name=Main.uuid("task \""+name+"\"");
		this.func=func;
	}
	
	public Task(JavaFunction func) {
		this("unnamed",func);
	}
	
	public Object run(Object...args) {
		Main.out.debug(name+" was called");
		Main.out.debug(Arrays.asList(args));
		i++;
		return func.run(args);
	}
	
	public String toString() {
		return name+" i="+i;
	}
	
	public void reset() {
		i=0;
	}
	
	public int i() {
		return i;
	}
}
