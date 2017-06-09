package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerMoveUnitsEvent extends PRET implements Cancelable {
	public Player player;
	public String[] spatialID;
	public float x, y, z;

	public PlayerMoveUnitsEvent(final Player player, final String[] spatialID, final float x, final float y,
			final float z) {
		this.player = player;
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
