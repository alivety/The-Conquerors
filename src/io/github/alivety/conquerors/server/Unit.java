package io.github.alivety.conquerors.server;

import io.github.alivety.conquerors.common.UnitObject;

public abstract class Unit extends UnitObject {
	protected Unit(final Server server) {
		super();
		server.registerUnit(this);
		}
	
	public abstract float[][] getForm();
}
