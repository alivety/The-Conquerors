package io.github.alivety.conquerors.common.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Common;

@Common
public class PlayerChatEvent extends Event implements Cancelable {
	public PlayerObject client;
	public String message;
	
	public PlayerChatEvent(final PlayerObject player, final String message) {
		this.client = player;
		this.message = message;
	}
	
	public PlayerChatEvent(final String message) {
		this.message = message;
	}
}
