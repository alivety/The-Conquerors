package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class PlayerMoveUnitsEvent extends Event implements Cancelable {
	public PlayerObject player;
	public String[] spatialID;
	public float x, y, z;
	
	public PlayerMoveUnitsEvent(final PlayerObject player, final String[] spatialID, final float x, final float y, final float z) {
		this.player = player;
		this.spatialID = spatialID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
