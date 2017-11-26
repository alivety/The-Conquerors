package io.github.alivety.conquerors.common;

import java.util.Vector;

public class Stack<T> {
	private final Vector<T> stack = new Vector<>();
	
	public boolean empty() {
		return this.stack.isEmpty();
	}
	
	public T pop() {
		this.stack.trimToSize();
		final T i = this.stack.get(0);
		this.stack.remove(i);
		return i;
	}
	
	public void push(final T r) {
		this.stack.addElement(r);
	}
	
}
