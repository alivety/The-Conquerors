package io.github.alivety.conquerors.client.events;

import com.jme3.math.ColorRGBA;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class LoginSuccessEvent extends Event {
	public String spatialID;
	public ColorRGBA team;
	
	public LoginSuccessEvent(final String spatialID, final ColorRGBA team) {
		this.spatialID = spatialID;
		this.team = team;
	}
}