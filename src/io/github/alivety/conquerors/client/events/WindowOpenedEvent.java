package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class WindowOpenedEvent extends PRET implements Cancelable {
	public String[] slots;

	public WindowOpenedEvent(final String[] slots) {
		this.slots = slots;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}
}
