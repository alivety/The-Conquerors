package io.github.alivety.conquerors.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.alivety.conquerors.Main;

public class EventBus {
	private ListenerList listeners=new ListenerList();
	protected static class EventListener implements Comparable<EventListener> {
		private final int priority;
		private final Object context;
		private final Method method;
		private final Class<Event> evt;
		protected EventListener(EventPriority priority,Object ctx,Method m,Class<Event> evt) {
			this.priority=priority.weight();
			this.context=ctx;
			this.method=m;
			this.evt=evt;
			
			this.method.setAccessible(true);
		}
		
		public int compareTo(EventListener el) {
			return (priority-el.priority)*-1;
		}
		
		protected void call(Event evt) {
			if (this.evt.isInstance(evt)) {
				try {
					method.invoke(context, evt);
				} catch (Exception e) {
					Main.handleError(e);
				}
			} else {
				Main.out.debug(this+": "+evt+" not instance of "+this.evt);
			}
		}
		
		public String toString() {
			return "EventListener[ctx="+context+"; method="+method+"; priority="+priority+"]";
		}
	}
	
	public void subscribe(Object target) {
		Method[] methods=target.getClass().getDeclaredMethods();
		List<EventListener> list=new ArrayList<EventListener>();
		for (Method m:methods) {
			if (m.isAnnotationPresent(SubscribeEvent.class)) {
				Class<?>[] paramTypes=m.getParameterTypes();
				if (paramTypes.length>1) throw new IllegalArgumentException("Event subscribers may only catch one event");
				if (paramTypes.length<1) throw new IllegalArgumentException("Event subscribes must catch one event");
				if (!((paramTypes[0].getSuperclass().equals(Event.class)) || paramTypes[0].equals(Event.class))) throw new IllegalArgumentException("Event subscribers must have a subclass of Event as the sole paramater");
				SubscribeEvent a=m.getAnnotation(SubscribeEvent.class);
				list.add(new EventListener(a.value(),target,m,(Class<Event>) paramTypes[0]));
			}
		}
		listeners.expand(list);
		Main.out.debug(listeners);
	}
	
	public void bus(Event evt) {
		while (listeners.hasMore()) {
			listeners.next().call(evt);
		}
		if (evt.isCanceled()) {
			Main.out.debug(evt+" is canceled");
			return;
		}
		evt.post();
	}
}
