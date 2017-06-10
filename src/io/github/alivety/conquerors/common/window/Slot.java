package io.github.alivety.conquerors.common.window;

import io.github.alivety.conquerors.common.PlayerObject;

public abstract class Slot {
	protected PlayerObject player;
	protected Slot(PlayerObject player) {
		this.player=player;
	}
	
	public abstract void click();
}
