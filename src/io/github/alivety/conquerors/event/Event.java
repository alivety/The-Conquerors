package io.github.alivety.conquerors.event;

import com.google.common.reflect.TypeToken;

public class Event {
	private boolean isCanceled=false;
	public boolean isCanceled() {
		return isCanceled;
	}
	
	public boolean isCancelable() {
		return TypeToken.of(this.getClass()).getTypes().interfaces().contains(TypeToken.of(Cancelable.class));
	}
	
	public void setCanceled(boolean canceled) {
		if (!isCancelable()) throw new UnsupportedOperationException("Cannot canel an event that does not implement Cancelable");
		this.isCanceled=canceled;
	}
	
	public void post() {}
	public final String toString() {
		return this.getClass().getSimpleName()+"[canceled="+isCanceled+"]";
	}
}
