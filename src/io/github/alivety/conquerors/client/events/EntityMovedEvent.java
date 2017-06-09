package io.github.alivety.conquerors.client.events;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class EntityMovedEvent extends PRET implements Cancelable {
	public @Nonnull String spatialID;
	public @Nonnull float x, y, z;

	public EntityMovedEvent(final String spatialID, final float x, final float y, final float z) {
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
