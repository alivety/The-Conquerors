package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class CreateModelEvent extends Event {
	public String spatialID;
	public float[] position;
	public float[][] form;
	
	public CreateModelEvent(final String spatialID, final float[] position, final float[][] form) {
		this.spatialID = spatialID;
		this.position = position;
		this.form = form;
	}
}
