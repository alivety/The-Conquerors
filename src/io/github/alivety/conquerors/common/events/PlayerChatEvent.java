package io.github.alivety.conquerors.common.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;

@Common
public class PlayerChatEvent extends PRET implements Cancelable {
	public PlayerObject client;
	public String message;

	public PlayerChatEvent(final PlayerObject player, final String message) {
		this.client = player;
		this.message = message;
	}

	public PlayerChatEvent(final String message) {
		this.message = message;
	}

	@Override
	public void post() {
		if (this.client == null) {// client side

		} else {// server side

		}
		// TODO server-side and client-side
	}

}
