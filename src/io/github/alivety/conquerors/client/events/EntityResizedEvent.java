package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class EntityResizedEvent extends PRET implements Cancelable {
	public String spatialID;
	public float x, y, z;

	public EntityResizedEvent(final String spatialID, final float x, final float y, final float z) {
		this.spatialID = spatialID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}
}
