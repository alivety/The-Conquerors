package io.github.alivety.conquerors.common;

import java.util.Arrays;

public class Task {
	private final String name;
	private final JavaFunction func;
	private int i = 0;

	public Task(final String name, final JavaFunction func) {
		this.name = Main.uuid("task \"" + name + "\"");
		this.func = func;
	}

	public Task(final JavaFunction func) {
		this("unnamed", func);
	}

	public Object run(final Object... args) {
		Main.out.debug(this.name + " was called");
		Main.out.debug(Arrays.asList(args));
		this.i++;
		return this.func.run(args);
	}

	@Override
	public String toString() {
		return this.name + " i=" + this.i;
	}

	public void reset() {
		this.i = 0;
	}

	public int i() {
		return this.i;
	}
}
