package io.github.alivety.conquerors.common.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import io.github.alivety.conquerors.common.event.EventBus.EventListener;

public class ListenerList {
	private final Stack<EventBus.EventListener> listeners = new Stack<EventListener>();
	private EventListener[] orginal;

	public ListenerList() {
		this(new ArrayList<EventListener>());
	}

	public ListenerList(final List<EventBus.EventListener> arr) {
		this.rebuild(arr);
	}

	public void expand(final List<EventListener> arr) {
		arr.addAll(Arrays.asList(this.orginal));
		this.rebuild(arr);
	}

	public boolean hasMore() {
		return !this.listeners.empty();
	}

	public EventListener next() {
		return this.listeners.pop();
	}

	public void rebuild() {
		this.rebuild(Arrays.asList(this.orginal));
	}

	private void rebuild(final List<EventListener> arr) {
		this.listeners.removeAllElements();
		Collections.sort(arr);
		final Iterator<EventListener> iter = arr.iterator();
		while (iter.hasNext())
			this.listeners.push(iter.next());
		this.orginal = new EventListener[this.listeners.size()];
		this.listeners.copyInto(this.orginal);
	}

	public int size() {
		return this.orginal.length;
	}

	@Override
	public String toString() {
		return this.listeners.toString();
	}
}
