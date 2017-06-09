package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class LoginSuccessEvent extends PRET {
	public String spatialID;

	public LoginSuccessEvent(final String spatialID) {
		this.spatialID = spatialID;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}
}
