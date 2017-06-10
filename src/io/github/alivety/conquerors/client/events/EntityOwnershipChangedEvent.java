package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class EntityOwnershipChangedEvent extends Event implements Cancelable {
	public String spatialID, ownerSpatialID;

	public EntityOwnershipChangedEvent(final String spatialID, final String ownerSpatialID) {
		this.spatialID = spatialID;
		this.ownerSpatialID = ownerSpatialID;
	}
}
