package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class LoginFailureEvent extends PRET {
	public String reason;

	public LoginFailureEvent(final String reason) {
		this.reason = reason;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
