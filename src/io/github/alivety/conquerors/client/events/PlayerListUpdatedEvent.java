package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside public class PlayerListUpdatedEvent extends Event {
	public String[] list;
	
	public PlayerListUpdatedEvent(final String[] list) {
		this.list = list;
	}
}
