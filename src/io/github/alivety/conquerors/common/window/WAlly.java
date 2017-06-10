package io.github.alivety.conquerors.common.window;

import java.util.ArrayList;
import java.util.List;

import io.github.alivety.conquerors.common.ConquerorsApp;
import io.github.alivety.conquerors.common.PlayerObject;

public class WAlly extends Window {
	private PlayerObject player;
	private ConquerorsApp app;
	public WAlly(PlayerObject p,ConquerorsApp app) {
		player=p;
	}
	
	public Slot[] getSlots() {
		List<Slot> slots=new ArrayList<Slot>();
		for (PlayerObject p : app.getOnlinePlayers()) {
			if (p.getSpatialID().equals(player.getSpatialID())) continue;
			slots.add(new Slot(p){
				@Override
				public void click() {
					//TODO alliance
				}});
		}
		return slots.toArray(new Slot[slots.size()]);
	}
}
