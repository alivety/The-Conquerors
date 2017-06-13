package io.github.alivety.conquerors.test.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;

public class DummyEvent extends Event implements Cancelable {
	public DummyEvent() {
		this.setCanceled(true);
	}
}