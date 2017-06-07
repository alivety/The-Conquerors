package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.Event;

public class DummyEvent extends Event implements Cancelable {
	public DummyEvent() {
		this.setCanceled(true);
	}
}