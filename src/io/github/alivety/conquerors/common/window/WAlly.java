package io.github.alivety.conquerors.common.window;

import java.util.ArrayList;
import java.util.List;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.PlayerObject;

public class WAlly extends Window {
	private final PlayerObject player;
	private ConquerorsApp app;

	public WAlly(final PlayerObject p, final ConquerorsApp app) {
		this.player = p;
	}

	public Slot[] getSlots() {
		final List<Slot> slots = new ArrayList<Slot>();
		for (final PlayerObject p : this.app.getOnlinePlayers()) {
			if (p.getSpatialID().equals(this.player.getSpatialID()))
				continue;
			slots.add(new Slot(this.player) {
				@Override
				public void click() {
					// TODO alliance
				}
			});
		}
		return slots.toArray(new Slot[slots.size()]);
	}
}
