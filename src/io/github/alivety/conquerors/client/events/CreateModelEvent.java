package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class CreateModelEvent extends Event {
	public String name;
	public int[] position;
	public int[][] form;
	
	public CreateModelEvent(final String name, final int[] position, final int[][] form) {
		this.name = name;
		this.position = position;
		this.form = form;
	}
}
