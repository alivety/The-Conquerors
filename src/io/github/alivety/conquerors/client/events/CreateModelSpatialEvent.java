package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class CreateModelSpatialEvent extends Event {
	public byte shape;
	public int[] vectors;
	
	public CreateModelSpatialEvent(final byte shape, final int[] vectors) {
		this.shape = shape;
		this.vectors = vectors;
	}
}
