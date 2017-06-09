package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class PlayerListUpdatedEvent extends PRET {
	public String[] list;

	public PlayerListUpdatedEvent(final String[] list) {
		this.list = list;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
