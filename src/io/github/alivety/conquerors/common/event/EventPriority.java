package io.github.alivety.conquerors.common.event;

public enum EventPriority {
	SYS(5), HIGHER(4), HIGH(3), LOW(2), LOWER(1);
	private int weight;
	
	private EventPriority(final int weight) {
		this.weight = weight;
	}
	
	public int weight() {
		return this.weight;
	}
}
