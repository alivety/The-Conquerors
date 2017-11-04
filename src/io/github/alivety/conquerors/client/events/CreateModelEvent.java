package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;

public class CreateModelEvent extends Event {
	public String name;
	public int[] position;
	public int[][] form;
	public CreateModelEvent(String name,int[]position,int[][]form) {
		this.name=name;
		this.position=position;
		this.form=form;
	}
}
