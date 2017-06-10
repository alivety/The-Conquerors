package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class LoginSuccessEvent extends Event {
	public String spatialID;

	public LoginSuccessEvent(final String spatialID) {
		this.spatialID = spatialID;
	}
}