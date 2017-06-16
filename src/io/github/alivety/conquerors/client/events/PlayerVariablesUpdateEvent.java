package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class PlayerVariablesUpdateEvent extends Event {
	public int money, mpm;
	public String[] unitSpatialID;

	public PlayerVariablesUpdateEvent(final int money, final int mpm, final String[] unitSpatialID) {
		this.money = money;
		this.mpm = mpm;
		this.unitSpatialID = unitSpatialID;
	}
}
