package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class PlayerMovedEvent extends PRET implements Cancelable {
	public PlayerObject client;
	public float x, y, z;

	public PlayerMovedEvent(final PlayerObject client, final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}