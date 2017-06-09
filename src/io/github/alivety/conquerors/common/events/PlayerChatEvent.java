package io.github.alivety.conquerors.common.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Common
public class PlayerChatEvent extends PRET implements Cancelable {
	public Player client;
	public String message;

	public PlayerChatEvent(final Player player, final String message) {
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
