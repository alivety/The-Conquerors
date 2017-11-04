package io.github.alivety.conquerors.client.events;

import java.nio.channels.SocketChannel;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class CreateModelSpatialEvent extends Event {
	public byte shape;
	public int[] vectors;
	
	public CreateModelSpatialEvent(byte shape,int[]vectors) {
		this.shape=shape;
		this.vectors=vectors;
	}
}
