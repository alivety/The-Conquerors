package io.github.alivety.conquerors.common.window;

import java.util.ArrayList;

public abstract class Window {
	public abstract Slot[] getSlots();
	public abstract String getTitle();
	public abstract String getDescription();
	
	public final String[] getSlotValues() {
		ArrayList<String> list=new ArrayList<>();
		for (Slot s:getSlots()) {
			list.add(s.getValue());
		}
		return list.toArray(new String[]{});
	}
	
	public void click(int slot) {
		getSlots()[slot].click();
	}
	
	public abstract static class Slot {
		public abstract void click();
		public abstract String getValue();
	}
}