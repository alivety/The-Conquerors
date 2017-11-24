package io.github.alivety.conquerors.common.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Window {
	protected abstract Slot[] w_getSlots();
	public abstract String getTitle();
	public abstract String getDescription();
	
	public final String[] getSlotValues() {
		ArrayList<String> list=new ArrayList<>();
		for (Slot s:getSlots()) {
			list.add(s.getValue());
		}
		return list.toArray(new String[]{});
	}
	
	public Slot[] getSlots() {
		List<Slot> slots=Arrays.asList(this.w_getSlots());
		slots.add(new Slot(){
			@Override
			public void click() {}

			@Override
			public String getValue() {
				return "Exit";
			}});
		return slots.toArray(new Slot[] {});
	}
	
	public void click(int slot) {
		getSlots()[slot].click();
	}
	
	public abstract static class Slot {
		public abstract void click();
		public abstract String getValue();
	}
}