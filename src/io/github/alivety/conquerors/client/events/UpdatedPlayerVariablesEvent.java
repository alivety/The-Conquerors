package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class UpdatedPlayerVariablesEvent extends PRET {
	public int money, mpm;
	public String[] unitSpatialID;

	public UpdatedPlayerVariablesEvent(final int money, final int mpm, final String[] unitSpatialID) {
		this.money = money;
		this.mpm = mpm;
		this.unitSpatialID = unitSpatialID;
	}

	@Override
	public void post() {

	}
}