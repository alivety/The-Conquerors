package io.github.alivety.conquerors.common.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import io.github.alivety.conquerors.common.Main;

public class EventBus {
	private final ListenerList listeners = new ListenerList();

	protected static class EventListener implements Comparable<EventListener> {
		private final int priority;
		private final Object context;
		private final Method method;
		private final Class<Event> evt;

		protected EventListener(final EventPriority priority, final Object ctx, final Method m,
				final Class<Event> evt) {
			this.priority = priority.weight();
			this.context = ctx;
			this.method = m;
			this.evt = evt;

			this.method.setAccessible(true);
		}

		public int compareTo(final EventListener el) {
			return (this.priority - el.priority) * -1;
		}

		protected void call(final Event evt) {
			if (this.evt.isInstance(evt))
				try {
					this.method.invoke(this.context, evt);
				} catch (final Exception e) {
					Main.handleError(e);
				}
		}

		@Override
		public String toString() {
			return "EventListener[ctx=" + this.context.getClass().getSimpleName() + "#" + this.method.getName()
					+ "; priority=" + this.priority + "]";
		}
	}

	public void subscribe(final Object target) {
		final Method[] methods = target.getClass().getDeclaredMethods();
		final List<EventListener> list = new ArrayList<EventListener>();
		for (final Method m : methods)
			if (m.isAnnotationPresent(SubscribeEvent.class)) {
				final Class<?>[] paramTypes = m.getParameterTypes();
				if (paramTypes.length > 1)
					throw new IllegalArgumentException("Event subscribers may only catch one event");
				if (paramTypes.length < 1)
					throw new IllegalArgumentException("Event subscribes must catch one event");

				final TypeToken<?> type = TypeToken.of(paramTypes[0]);
				final TypeToken<?> event = TypeToken.of(Event.class);
				if (!type.getTypes().contains(event))
					throw new IllegalArgumentException(
							"Event subscribers must have a subclass of Event as the sole paramater");
				final SubscribeEvent a = m.getAnnotation(SubscribeEvent.class);
				list.add(new EventListener(a.value(), target, m, (Class<Event>) paramTypes[0]));
			}
		this.listeners.expand(list);
		Main.out.debug(this.listeners.size() + " listeners");
		Main.out.debug(this.listeners);
	}

	public void bus(final Event evt) {
		this.listeners.rebuild();
		while (this.listeners.hasMore()) {
			final EventListener l = this.listeners.next();
			if (l.priority == 5)
				if (evt.isCanceled())
					continue;
			Main.out.debug(l + " ~ " + evt);
			l.call(evt);
		}
	}
}
