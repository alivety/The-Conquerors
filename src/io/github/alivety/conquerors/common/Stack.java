package io.github.alivety.conquerors.common;

import java.util.Vector;

public class Stack<T> {
	private Vector<T> stack=new Vector<>();
	public boolean empty() {
		return stack.isEmpty();
	}

	public T pop() {
		stack.trimToSize();
		T i=stack.get(0);
		stack.remove(i);
		return i;
	}

	public void push(T r) {
		stack.addElement(r);
	}
	
}
