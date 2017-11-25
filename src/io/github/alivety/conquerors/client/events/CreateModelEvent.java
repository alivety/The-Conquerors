package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class CreateModelEvent extends Event {
	public String spatialID;
	public int[] position;
	public int[][] form;
	
	public CreateModelEvent(final String spatialID, final int[] position, final int[][] form) {
		this.spatialID = spatialID;
		this.position = position;
		this.form = form;
	}
}
