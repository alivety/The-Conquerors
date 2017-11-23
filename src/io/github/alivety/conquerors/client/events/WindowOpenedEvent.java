package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class WindowOpenedEvent extends Event implements Cancelable {
	public String spatialID;
	public String[] slots;
	
	public WindowOpenedEvent(String spatialID, final String[] slots) {
		this.spatialID=spatialID;
		this.slots = slots;
	}
}
