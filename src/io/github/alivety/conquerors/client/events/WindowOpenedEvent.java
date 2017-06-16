package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class WindowOpenedEvent extends Event implements Cancelable {
	public String[] slots;
	
	public WindowOpenedEvent(final String[] slots) {
		this.slots = slots;
	}
}
