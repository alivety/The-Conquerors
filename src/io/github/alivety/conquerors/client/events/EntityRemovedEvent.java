package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class EntityRemovedEvent extends Event {
	public String spatialID;
	
	public EntityRemovedEvent(final String spatialID) {
		this.spatialID = spatialID;
	}
}
