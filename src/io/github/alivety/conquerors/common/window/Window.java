package io.github.alivety.conquerors.common.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Window {
	protected abstract Slot[] w_getSlots();
	
	public abstract String getTitle();
	
	public abstract String getDescription();
	
	public final String[] getSlotValues() {
		final ArrayList<String> list = new ArrayList<>();
		for (final Slot s : this.getSlots())
			list.add(s.getValue());
		return list.toArray(new String[] {});
	}
	
	public Slot[] getSlots() {
		final List<Slot> slots = Arrays.asList(this.w_getSlots());
		slots.add(new Slot() {
			@Override
			public void click() {}
			
			@Override
			public String getValue() {
				return "Exit";
			}
		});
		return slots.toArray(new Slot[] {});
	}
	
	public void click(final int slot) {
		this.getSlots()[slot].click();
	}
	
	public abstract static class Slot {
		public abstract void click();
		
		public abstract String getValue();
	}
}