package io.github.alivety.conquerors.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import io.github.alivety.conquerors.event.EventBus.EventListener;

public class ListenerList {
	private Stack<EventBus.EventListener> listeners=new Stack<EventListener>();
	private EventListener[] orginal;
	public ListenerList(List<EventBus.EventListener> arr) {
		this.rebuild(arr);
	}
	
	public ListenerList() {
		this(new ArrayList<EventListener>());
	}
	
	public String toString() {
		return listeners.toString();
	}
	
	public void expand(List<EventListener> arr) {
		arr.addAll(Arrays.asList(orginal));
		this.rebuild(arr);
	}
	
	private void rebuild(List<EventListener> arr) {
		listeners.removeAllElements();
		Collections.sort(arr);
		Iterator<EventListener> iter=arr.iterator();
		while (iter.hasNext()) {
			listeners.push(iter.next());
		}
		orginal=new EventListener[listeners.size()];
		listeners.copyInto(orginal);
	}
	
	public boolean hasMore() {
		return !listeners.empty();
	}
	
	public EventListener next() {
		return listeners.pop();
	}
	
	public void rebuild() {
		this.rebuild(Arrays.asList(orginal));
	}
	
	public int size() {
		return orginal.length;
	}
}
