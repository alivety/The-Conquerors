package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Serverside;

@Serverside
public class WindowSlotSelectedEvent extends Event implements Cancelable {
	public PlayerObject player;
	public String spatialID;
	public int slot;
	
	public WindowSlotSelectedEvent(final PlayerObject player, final String spatialID, final int slot) {
		this.player = player;
		this.spatialID = spatialID;
		this.slot = slot;
	}
}
