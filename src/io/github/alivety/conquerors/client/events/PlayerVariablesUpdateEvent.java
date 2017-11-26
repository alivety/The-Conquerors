package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class PlayerVariablesUpdateEvent extends Event {
	public int money, mpm;
	public String[] unitSpatialID;
	public String[] alliance;
	
	public PlayerVariablesUpdateEvent(final int money, final int mpm, final String[] unitSpatialID, final String[] alliance) {
		this.money = money;
		this.mpm = mpm;
		this.unitSpatialID = unitSpatialID;
		this.alliance = alliance;
	}
}
