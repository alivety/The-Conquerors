package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class EntitySpawnEvent extends Event implements Cancelable {
	public String model, material, spatialID;

	public EntitySpawnEvent(final String model, final String material, final String spatialID) {
		this.model = model;
		this.material = material;
		this.spatialID = spatialID;
	}
}
