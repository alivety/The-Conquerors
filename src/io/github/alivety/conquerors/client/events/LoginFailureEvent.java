package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class LoginFailureEvent extends Event {
	public String reason;
	
	public LoginFailureEvent(final String reason) {
		this.reason = reason;
	}
}
