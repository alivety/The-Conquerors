package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerMovedEvent extends PRET implements Cancelable {
	public Player client;
	public float x, y, z;

	public PlayerMovedEvent(final Player client, final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
